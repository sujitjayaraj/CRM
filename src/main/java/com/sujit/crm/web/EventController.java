package com.sujit.crm.web;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Event;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.EventRepository;
import com.sujit.crm.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ClientService clientService;
    @Autowired
    private EventRepository eventRepository;
    @GetMapping("/new")
    public String newEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        User user = authenticationFacade.getAuthenticatedUser();
        List<Client> clientList = clientService.findByUser(user);
        model.addAttribute("clientList", clientList);
        return "event/new";
    }
    @PostMapping("/new")
    public String registerEvent(@RequestParam String stringTime, @ModelAttribute("event") @Valid Event event, BindingResult result, Model model){
        if(result.hasErrors()){
            return "event/new";
        }
        else{
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                event.setCreatedAt(LocalDateTime.parse(stringTime, formatter));
            }
            catch (Exception e){
                e.printStackTrace();
                return "event/new";
            }
        }
        User user = authenticationFacade.getAuthenticatedUser();
        event.setUser(user);
        eventRepository.save(event);
        String res = "Event created successfully";
        model.addAttribute("result", res);
        return "event/result";
    }

}