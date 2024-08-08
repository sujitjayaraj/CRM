package com.sujit.crm.api;

import com.sujit.crm.entity.User;
import com.sujit.crm.repository.OfficeRepository;
import com.sujit.crm.repository.RoleRepository;
import com.sujit.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;

@Controller
@RequestMapping("/admin")
public class AdminResource {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @GetMapping(path = "/addUser")
    public String addUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", new HashSet<>(roleRepository.findAll()));
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("allOffices", officeRepository.findAll());
        return "admin/addUser";
    }

    @PostMapping(path = "/addUser")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bresult) {
        if(bresult.hasErrors()){
            return "admin/addUser";
        }

        userService.saveUser(user);
        return "redirect:/admin/addUser";
    }
}
