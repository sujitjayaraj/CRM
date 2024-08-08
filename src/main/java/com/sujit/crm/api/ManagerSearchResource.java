package com.sujit.crm.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
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
public class ManagerSearchResource {
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
    public void addAttributes(Model model) {
        model.addAttribute("statusList", clientService.getStatusList());
        model.addAttribute("cityList", addressService.getCitiesList());
    }

    @GetMapping
    public String defaultSearch(Model model) {
        model.addAttribute("clientList", clientRepository.findByUserIn(userService.findBySupervisor(authenticationFacade.getAuthenticatedUser())));
        return "managerSearch/search";
    }

    @GetMapping("/all")
    public String allSearch(Model model) {
        model.addAttribute("clientList", clientRepository.findAll());
        return "manager/search";
    }

    @PostMapping(path = "/city")
    public String citySearch(@RequestParam String city, Model model) {
        model.addAttribute("clientList", clientRepository.findByAddressCityContainingIgnoreCaseOrderByNameAsc(city));
        return "manager/search";
    }

    @GetMapping(path = "/state")
    public String citySearch(Model model) {
        model.addAttribute("clientList", clientRepository.findByAddressStateOrderByNameAsc(authenticationFacade.getAuthenticatedUser().getOffice().getAddress().getState()));
        return "manager/search";
    }

    @PostMapping(path = "/name")
    public String nameSearch(@RequestParam String name, Model model) {
        model.addAttribute("clientList", clientRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name));
        return "manager/search";
    }

    @GetMapping(path = "/print")
    public String printSearch(Model model) {
        String filename = "pdf/" + authenticationFacade.getAuthenticatedUser().getLastname() + "_" + search + "_" + LocalDateTime.now();
        String result = pdfService.printClientList(filename, clientList);
        model.addAttribute("result", result);
        return "manager/result";
    }

    @PostMapping(path = "/export")
    public String exportSearch(@RequestParam String filename, Model model) {
        String newFilename = "csvExport/" + filename + "_" + authenticationFacade.getAuthenticatedUser().getLastname();
        String result = "File " + newFilename + ".csv generated correctly.";

        try {
            csvService.writeCsv(newFilename, clientList);
        } catch (CsvDataTypeMismatchException e) {
            result = "Document not generated due to type mismatch.";
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            result = "Document not generated as required field is empty.";
            e.printStackTrace();
        } catch (IOException e) {
            result = "Document not generated due to an error.";
            e.printStackTrace();
        }

        model.addAttribute("result", result);
        return "manager/result";
    }
}
