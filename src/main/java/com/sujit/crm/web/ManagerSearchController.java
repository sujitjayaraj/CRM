package com.sujit.crm.web;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.ClientRepository;
import com.sujit.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/managerSearch")
public class ManagerSearchController {
    private List<Client> clientList;
    private String search;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private CsvImportExportService csvService;
    @ModelAttribute
    public void addAttributes(Model model){
        model.addAttribute("statusList", clientService.getStatusList());
        model.addAttribute("cityList", addressService.getCitiesList());
    }
    @GetMapping
    public String defaultSearch(Model model){
        User user = authenticationFacade.getAuthenticatedUser();
        List<User> employees = userService.findBySupervisor(user);
        clientList = clientRepository.findByUserIn(employees);
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @GetMapping("/all")
    public String allSearch(Model model){
        clientList = clientRepository.findAll();
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @PostMapping(path = "/city")
    public String citySearch(@RequestParam String city, Model model){
        clientList = clientRepository.findByAddressCityOrderByNameAsc(city);
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @GetMapping(path = "/state")
    public String citySearch(Model model){
        User user = authenticationFacade.getAuthenticatedUser();
        clientList = clientRepository.findByAddressStateOrderByNameAsc(user.getOffice().getAddress().getState());
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @PostMapping(path = "/status")
    public String statusSearch(@RequestParam String status, Model model) {
        clientList = clientRepository.findByStatusOrderByNameAsc(status);
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @PostMapping(path = "/name")
    public String nameSearch(@RequestParam String name, Model model) {
        clientList = clientRepository.findByNameOrderByNameAsc(name);
        model.addAttribute("clientList", clientList);
        return "manager/search";
    }
    @GetMapping(path = "/print")
    public String printSearch(Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        String filename = "pdf/" + user.getLastName() + "_" + search + "_" + LocalDateTime.now();
        String result = pdfService.printClientList(filename, clientList);
        model.addAttribute("result", result);
        return "manager/result";
    }
    @PostMapping(path = "/export")
    public String exportSearch(@RequestParam String filename, Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        String newFilename = "csvExport/" + filename + "_" + user.getLastName();
        String result = "File " + newFilename + " .csv generated correctly.";
        try {
            csvService.writeCsv(newFilename, clientList);
        }
        catch(CsvDataTypeMismatchException e) {
            result = "Document not generated due to data type mismatch";
            e.printStackTrace();
        }
        catch(CsvRequiredFieldEmptyException e) {
            result = "Document not generated due to required field empty";
            e.printStackTrace();
        }
        catch(IOException e) {
            result = "Document not generated due to I/O error";
            e.printStackTrace();
        }
        model.addAttribute("result", result);
        return "manager/result";
    }
}