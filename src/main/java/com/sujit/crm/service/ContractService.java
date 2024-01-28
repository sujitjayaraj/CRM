package com.sujit.crm.service;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.ContractRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Log4j2
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
    ContractService(ContractRepository contractRepository, UserService userService, PdfService pdfService, AuthenticationFacade authenticationFacade){
        super();
        this.contractRepository = contractRepository;
        this.userService = userService;
        this.pdfService = pdfService;
        this.authenticationFacade = authenticationFacade;
    }

    /**Uses PDF service to generate PDF for given Contract
     *
     * @param contract Contract
     * @throws FileNotFoundException FileNotFoundException
     */
    public void print(Contract contract) throws FileNotFoundException {
        pdfService.printContract(contract);
    }
    public boolean sendToSupervisor(Contract contract, User user){
        User supervisor;
        while(true){
            supervisor = user.getSupervisor();
            user = supervisor;
            if(supervisor == null){
                return false;
            }
            if(contract.getValue() <= userService.getMaxContractValue(supervisor)){
                contract.setAcceptedBy(supervisor);
                this.save(contract);
                return true;
            }
        }
    }

    /**Accepts given Contract if logged User is permitted to do so
     *
     * @param contract Contract
     */
    public void accept(Contract contract){
        User user = authenticationFacade.getAuthenticatedUser();
        if(contract.getAcceptedBy().equals(user)) {
            contract.setAcceptedBy(user);
            this.save(contract);
        }
    }

    /**Returns Contract object with given id
     *
     * @param id Long id
     * @return Contract object with given id
     */
    public Contract findById(Long id){
        return contractRepository.findById(id).orElse(null);
    }

    /**Saves given Contract to database
     *
     * @param contract Contract
     */
    public void save(Contract contract) {
        contractRepository.save(contract);
    }

    /**Returns List of Contracts for given Client
     *
     * @param client Client
     * @return List of Contracts for given Client
     */
    public List<Contract> findByClient(Client client){
        return contractRepository.findByClient(client);
    }

    /**Returns List of Contracts for given User who is responsible for accepting contract
     *
     * @param user responsible for accepting the contract
     * @return list of Contracts
     */
    public List<Contract> findByAcceptedBy(User user){
        return contractRepository.findByAcceptedBy(user);
    }
}