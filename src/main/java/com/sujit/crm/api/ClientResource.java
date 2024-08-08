package com.sujit.crm.api;

import com.sujit.crm.entity.Client;
import com.sujit.crm.repository.EventRepository;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.ContractService;
import com.sujit.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientResource {
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping(path = "/add")
    public String addClient(Model model){
        model.addAttribute("client", new Client());
        model.addAttribute(clientService.getStatusList());
        return "client/addClient";
    }

    @GetMapping(path = "/edit/{id}")
    public String editClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("statusList", clientService.getStatusList());
        return "client/editClient";
    }

    @PostMapping(path = "/edit/{id}")
    public String saveClient(@ModelAttribute("client") @Valid Client client, BindingResult bresult, @PathVariable Long id) {
        if (bresult.hasErrors()) {
            return "client/editClient";
        } else {
            client.setId(id);
            client.setUser(userService.findByEmail(client.getUser().getEmail()));
            clientService.saveClient(client);
            return "client/success";
        }
    }

    @GetMapping(path = "/details/{id}")
    public String clientDetails(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("contractList", contractService.findByClient(client));
        model.addAttribute("eventList", eventRepository.findByClient(client));
        return "client/clientDetails";
    }

    @GetMapping(path = "/addSimilar/{id}")
    public String addSimilarClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("statusList", clientService.getStatusList());
        return "client/addSimilarClient";
    }
}
