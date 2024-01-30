package com.sujit.crm.web;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.ContractService;
import com.sujit.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @GetMapping("/new/{id}")
    public String newContract(@PathVariable Long id, Model model){
        Contract contract = new Contract();
        model.addAttribute("contract", contract);
        model.addAttribute("id", id);
        return "contract/new";
    }
    @PostMapping("/new/{id}")
    public String registerUser(@PathVariable Long id, Contract contract, Model model, BindingResult result){
        if(result.hasErrors()){
            return "contract/new";
        }
        else {
            String reply;
            User user = authenticationFacade.getAuthenticatedUser();
            Client client = clientService.findById(id);
            contract.setId(null);
            contract.setClient(client);
            contract.setAuthor(user);
            contract.setCreatedAt(LocalDateTime.now());
            contract.setFilename("contracts/"+contract.getFilename());

            if(contract.getValue() < userService.getMaxContractValue(user)){
                contract.setAcceptedBy(user);
                contract.setAccepted(true);
                contractService.save(contract);
                reply = "Contract generated";
            }
            else if(contractService.sendToSupervisor(contract, user)){
                reply = "Contract sent to supervisor for approval";
            }
            else{
                reply = "You do not have a supervisor to approve this contract";
            }
            model.addAttribute("result", reply);
            return "contract/result";
        }
    }
}
