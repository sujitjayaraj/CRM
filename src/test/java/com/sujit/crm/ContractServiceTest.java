package com.sujit.crm;

import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.ContractRepository;
import com.sujit.crm.service.ContractService;
import com.sujit.crm.service.PdfService;
import com.sujit.crm.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

public class ContractServiceTest {

    private ContractService contractService;
    private UserService userService;
    private PdfService pdfService;
    private AuthenticationFacade authenticationFacade;
    private ContractRepository contractRepository;

    @Before
    public void setUp() {
        contractRepository = Mockito.mock(ContractRepository.class);
        userService = Mockito.mock(UserService.class);
        pdfService = Mockito.mock(PdfService.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        contractService = new ContractService(contractRepository, userService, pdfService, authenticationFacade);
    }

    @Test
    public void given_contract_and_user_when_supervisor_can_accept_then_acceptedBy_supervisor() {
        User user = new User();
        User supervisor = new User();
        Contract contract = new Contract();
        user.setSupervisor(supervisor);
        contract.setValue(100.00);
        Mockito.when(userService.getMaxContractValue(supervisor)).thenReturn(1000.00);
        contractService.sendToSupervisor(contract, user);
        Assert.assertEquals(contract.getAcceptedBy(), supervisor);
    }

    @Test
    public void given_contract_and_user_when_supervisor_cant_accept_then_send_to_his_supervisor() {
        User user = new User();
        User supervisor = new User();
        User nextSupervisor = new User();
        Contract contract = new Contract();
        contract.setValue(5000.00);
        user.setSupervisor(supervisor);
        supervisor.setSupervisor(nextSupervisor);
        Mockito.when(userService.getMaxContractValue(supervisor)).thenReturn(1000.00);
        Mockito.when(userService.getMaxContractValue(nextSupervisor)).thenReturn(10000.00);
        contractService.sendToSupervisor(contract, user);
        Assert.assertEquals(contract.getAcceptedBy(), nextSupervisor);
    }

    @Test
    public void given_user_who_can_accept_contract_then_contract_accepted() {
        User user = new User();
        Contract contract = new Contract();
        contract.setAcceptedBy(user);
        contract.setAccepted(false);
        Mockito.when(authenticationFacade.getAuthenticatedUser()).thenReturn(user);
        contractService.acceptContract(contract);
        Assert.assertTrue(contract.isAccepted());
    }

    @Test
    public void given_user_who_cant_accept_contract_then_contract_not_accepted() {
        User user = new User();
        User supervisor = new User();
        user.setSupervisor(supervisor);
        Contract contract = new Contract();
        contract.setAcceptedBy(supervisor);
        contract.setAccepted(false);
        Mockito.when(authenticationFacade.getAuthenticatedUser()).thenReturn(user);
        contractService.acceptContract(contract);
        Assert.assertFalse(contract.isAccepted());
    }
}
