package com.sujit.crm.web;

import com.sujit.crm.entity.Office;
import com.sujit.crm.entity.Role;
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
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @GetMapping(path = "/addUser")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        Set<Role> allRoles = new HashSet<>(roleRepository.findAll());
        model.addAttribute("allRoles", allRoles);
        List<User> allUsers = userService.findAll();
        model.addAttribute("allUsers", allUsers);
        List<Office> allOffices = officeRepository.findAll();
        model.addAttribute("allOffices", allOffices);
        return "admin/addUser";
    }
    @PostMapping(path = "/addUser")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bresult) {
        if(bresult.hasErrors()) {
            return "admin/addUser";
        }
        else{
            userService.saveUser(user);
            return "admin/success";
        }
    }
}
