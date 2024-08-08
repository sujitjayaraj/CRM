package com.sujit.crm.api;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.ContractService;
import com.sujit.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract")
public class ContractResource {
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
        model.addAttribute("contract", new Contract());
        model.addAttribute("id", id);
        return "contract/new";
    }

    @PostMapping("/new/{id}")
    public String registerUser(@PathVariable Long id, @ModelAttribute("contract") @Valid Contract contract, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            return "contract/new";
        } else {
            String result;
            User user = authenticationFacade.getAuthenticatedUser();
            contract.setId(null);
            contract.setClient(clientService.findById(id));
            contract.setAuthor(user);
            contract.setCreated(LocalDateTime.now());
            contract.setFilename("contracts/" + contract.getFilename());

            if (contract.getValue() < userService.getMaxContractValue(user)) {
                contract.setAcceptedBy(user);
                contract.setAccepted(true);
                contractService.save(contract);
                result = "Contract generated";
            } else if (contractService.sendToSupervisor(contract, user)) {
                result = "Contract sent to supervisor";
            } else {
                result = "You do not have a supervisor who can accept this contract";
            }

            model.addAttribute("result", result);
            return "contract/result";
        }
    }

    @GetMapping("/accept/{id}")
    public String acceptContract(@PathVariable Long id, Model model) {
        Contract contract = contractService.findById(id);
        String result;

        if (contract.getAcceptedBy().equals(authenticationFacade.getAuthenticatedUser())) {
            contractService.acceptContract(contract);
            result = "Contract accepted";
        } else {
            result = "You are not authorized to accept this contract";
        }

        model.addAttribute("result", result);
        return "contract/result";
    }

    @GetMapping("/print/{id}")
    public String printContract(@PathVariable Long id, Model model) {
        Contract contract = contractService.findById(id);
        String result;

        if (contract.isAccepted()) {
            try {
                contractService.print(contract);
                result = "Contract printed";
            } catch (FileNotFoundException e) {
                result = "Error while printing contract. File not found.";
                e.printStackTrace();
            }
        } else {
            result = "Contract not accepted yet";
        }

        model.addAttribute("result", result);
        return "contract/result";
    }
}
