package com.sujit.crm.web;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.ClientRepository;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.CsvImportExportService;
import com.sujit.crm.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/employeeSearch")
public class EmployeeSearchController {
    private List<Client> clientList;
    private String search;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private CsvImportExportService csvService;
    @ModelAttribute
    public void addAttributes(Model model){
        model.addAttribute("statusList", clientService.getStatusList());
    }
    @GetMapping
    public String defaultSearch(Model model){
        User user = authenticationFacade.getAuthenticatedUser();
        clientList = clientRepository.findByUser(user);
        model.addAttribute("clientList", clientList);
        search = "clients_managed_by" + user.getLastName();
        return "employee/search";
    }
    @GetMapping(path = "/city")
    public String citySearch(Model model){
        User user = authenticationFacade.getAuthenticatedUser();
        clientList = clientRepository.findByAddressCityOrderByNameAsc(user.getOffice().getAddress().getCity());
        model.addAttribute("clientList", clientList);
        search = "clients_from_" + user.getOffice().getAddress().getCity();
        return "employee/search";
    }
    @PostMapping(path = "/status")
    public String statusSearch(@RequestParam String status, Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        clientList = clientRepository.findByStatusAndAddressCityOrderByNameAsc(status, user.getOffice().getAddress().getCity());
        model.addAttribute("clientList", clientList);
        search = "clients_with_status_" + status;
        return "employee/search";
    }
    @PostMapping(path = "/name")
    public String nameSearch(@RequestParam String name, Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        clientList = clientRepository.findByNameAndAddressCityOrderByNameAsc(name, user.getOffice().getAddress().getCity());
        model.addAttribute("clientList", clientList);
        search = "clients_with_name_" + name;
        return "employee/search";
    }
    @GetMapping(path = "/print")
    public String printSearch(Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        String filename = "pdf/" + user.getLastName() + "_" + search + "_" + LocalDateTime.now();
        String result = pdfService.printClientList(filename, clientList);
        model.addAttribute("result", result);
        return "employee/result";
    }
    @PostMapping(path = "/export")
    public String exportSearch(@RequestParam String filename, Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        String newFilename = "csvExport/" + filename + "_" + user.getLastName();
        String result = "File " + newFilename + ".csv generated correctly";

        try{
            csvService.writeCsv(newFilename, clientList);
        }
        catch(CsvDataTypeMismatchException e){
            result = "Document not generated due to data type mismatch";
            e.printStackTrace();
        }
        catch(CsvRequiredFieldEmptyException e){
            result = "Document not generated due to required field empty";
            e.printStackTrace();
        }
        catch(IOException e){
            result = "Document not generated due to I/O error";
            e.printStackTrace();
        }
        model.addAttribute("result", result);
        return "employee/result";
    }
}