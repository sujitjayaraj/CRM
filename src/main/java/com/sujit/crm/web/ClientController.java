package com.sujit.crm.web;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.Event;
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

import java.util.List;

@Controller
@RequestMapping("clients")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private EventRepository eventRepository;
    @GetMapping(path = "/add")
    public String addClient(Model model) {
        Client client = new Client();
        model.addAttribute("client", client);
        List<String> statusList = clientService.getStatusList();
        model.addAttribute("statusList", statusList);
        return "client/addClient";
    }
    @PostMapping(path = "/add")
    public String registerClient(@ModelAttribute("client") @Valid Client client, BindingResult result) {
        if(result.hasErrors()) {
            return "client/addClient";
        }
        else {
            clientService.saveClientWithLoggedUser(client);
            return  "client/success";
        }
    }
    @GetMapping(path = "/edit/{id}")
    public String editClient(@PathVariable Long id, Model model){
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        List<String> statusList = clientService.getStatusList();
        model.addAttribute("statusList", statusList);
        return "client/editClient";
    }
    @PostMapping(path = "/edit/{id}")
    public String saveClient(@ModelAttribute("client") @Valid Client client, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()) {
            return "client/editClient";
        }
        else {
            client.setId(id);
            client.setUser(userService.findByEmail(client.getUser().getEmail()));
            clientService.saveClient(client);
            return  "client/success";
        }
    }
    @GetMapping(path = "/details/{id}")
    public String clientDetails(@PathVariable Long id, Model model){
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        List<Contract> contractList = contractService.findByClient(client);
        model.addAttribute("contractList", contractList);
        List<Event> eventList = eventRepository.findByClient(client);
        model.addAttribute("eventList", eventList);
        return "client/clientDetails";
    }
    @GetMapping(path = "/addSimilar/{id}")
    public String addSimilarClient(@PathVariable Long id, Model model){
        Client client = clientService.findById(id);
        model.addAttribute("client", client);
        List<String> statusList = clientService.getStatusList();
        model.addAttribute("statusList", statusList);
        return "client/addSimilarClient";
    }
}