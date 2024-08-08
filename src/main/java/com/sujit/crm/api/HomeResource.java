package com.sujit.crm.api;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.NotificationRepository;
import com.sujit.crm.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeResource {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ContractService contractService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/")
    public String home(Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        model.addAttribute("user", user);
        model.addAttribute("notificationList", notificationRepository.findByUser(user));
        model.addAttribute("contractList", contractService.findByAcceptedBy(user));
        return "index";
    }
}
