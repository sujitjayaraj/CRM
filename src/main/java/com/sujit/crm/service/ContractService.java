package com.sujit.crm.service;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.ContractRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Log4j
@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    public ContractService(ContractRepository contractRepository, UserService userService, PdfService pdfService, AuthenticationFacade authenticationFacade) {
        super();
        this.contractRepository = contractRepository;
        this.userService = userService;
        this.pdfService = pdfService;
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Generate PDF for given {@link com.sujit.crm.entity.Contract}.
     *
     * @param contract {@link com.sujit.crm.entity.Contract} to generate PDF for.
     * @throws FileNotFoundException if the file is not found.
     */
    public void print(Contract contract) throws FileNotFoundException {
        pdfService.printContract(contract);
    }

    /**
     * Sends given {@link com.sujit.crm.entity.Contract} to supervisor of currently logged {@link com.sujit.crm.entity.User}.
     *
     * @param contract {@link com.sujit.crm.entity.Contract} to send.
     * @param user {@link com.sujit.crm.entity.User} to send contract for.
     * @return true if contract is sent successfully, false otherwise.
     */
    public boolean sendToSupervisor(Contract contract, User user) {
        User supervisor;
        
        while (true) {
            supervisor = user.getSupervisor();
            user = supervisor;
            
            if (supervisor == null) {
                return false;
            }
            
            if (contract.getValue() <= userService.getMaxContractValue(supervisor)) {
                contract.setAcceptedBy(supervisor);
                this.save(contract);
                return true;
            }
        }
    }

    /**
     * Saves given contract to database.
     *
     * @param contract {@link com.sujit.crm.entity.Contract} to save.
     */
    public void save(Contract contract) {
        contractRepository.save(contract);
    }

    /**
     * Accepts given {@link com.sujit.crm.entity.Contract} by currently logged {@link com.sujit.crm.entity.User}.
     *
     * @param contract {@link com.sujit.crm.entity.Contract} to accept.
     */
    public void acceptContract(Contract contract) {
        User user = authenticationFacade.getAuthenticatedUser();

        if (contract.getAcceptedBy().equals(user)) {
            contract.setAccepted(true);
            this.save(contract);
        }
    }

    /**
     * Returns contract with given id
     *
     * @param id ID of the contract.
     * @return {@link com.sujit.crm.entity.Contract} with given id.
     */
    public Contract findById(Long id) {
        return contractRepository.findById(id).orElse(null);
    }

    /**
     * Returns a {@link java.util.List} of {@link com.sujit.crm.entity.Contract} for give {@link com.sujit.crm.entity.Client}.
     * @param client {@link com.sujit.crm.entity.Client} to get contracts for.
     * @return {@link java.util.List} of {@link com.sujit.crm.entity.Contract} for given {@link com.sujit.crm.entity.Client}.
     */
    public List<Contract> findByClient(Client client) {
        return contractRepository.findByClient(client);
    }

    /**
     * Returns a {@link java.util.List} of {@link com.sujit.crm.entity.Contract} accepted by given {@link com.sujit.crm.entity.User}.
     * @param user {@link com.sujit.crm.entity.User} to get accepted contracts for.
     * @return {@link java.util.List} of {@link com.sujit.crm.entity.Contract} accepted by given {@link com.sujit.crm.entity.User}.
     */
    public List<Contract> findByAcceptedBy(User user) {
        return contractRepository.findByAcceptedBy(user);
    }
}
