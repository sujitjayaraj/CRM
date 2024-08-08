package com.sujit.crm.api;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Event;
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

@Controller
@RequestMapping("/event")
public class EventResource {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ClientService clientService;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/new")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("clientList", clientService.findByUser(authenticationFacade.getAuthenticatedUser()));
        return "event/new";
    }

    @PostMapping("/new")
    public String registerEvent(@RequestParam String stringTime, @ModelAttribute("event") @Valid Event event, BindingResult bresult, Model model) {
        if (bresult.hasErrors()) {
            return "event/new";
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                event.setTime(LocalDateTime.parse(stringTime, formatter));
            } catch (Exception e) {
                e.printStackTrace();
                return "event/new";
            }
        }

        event.setUser(authenticationFacade.getAuthenticatedUser());
        eventRepository.save(event);
        String result = "Event created";
        model.addAttribute("result", result);
        return "event/result";
    }
}
