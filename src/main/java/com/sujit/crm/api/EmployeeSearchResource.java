package com.sujit.crm.api;

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
public class EmployeeSearchResource {
    private List<Client> clientList;
    private String search;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private CsvImportExportService csvService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("statusList", clientService.getStatusList());
    }

    @GetMapping
    public String defaultSearch(Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        model.addAttribute("clientList", clientRepository.findByUser(user));
        search = "clients_managed_by_" + user.getLastname();
        return "employee/search";
    }

    @GetMapping(path = "/city")
    public String citySearch(Model model) {
        User user = authenticationFacade.getAuthenticatedUser();
        model.addAttribute("clientList", clientRepository.findByAddressCityOrderByNameAsc(user.getOffice().getAddress().getCity()));
        search = "clients_from_" + user.getOffice().getAddress().getCity();
        return "employee/search";
    }

    @PostMapping(path = "/status")
    public String statusSearch(@RequestParam String status, Model model) {
        model.addAttribute("clientList", clientRepository.findByStatusAndAddressCityOrderByNameAsc(status, authenticationFacade.getAuthenticatedUser().getOffice().getAddress().getCity()));
        search = "clients_with_status_" + status;
        return "employee/search";
    }

    @PostMapping(path = "/name")
    public String nameSearch(@RequestParam String name, Model model) {
        model.addAttribute("clientList", clientRepository.findByNameContainingIgnoreCaseAndAddressCityOrderByNameAsc(name, authenticationFacade.getAuthenticatedUser().getOffice().getAddress().getCity()));
        search = "clients_with_name_" + name;
        return "employee/search";
    }

    @GetMapping(path = "/print")
    public String printSearch(Model model) {
        String filename = "pdf/" + authenticationFacade.getAuthenticatedUser().getLastname() + "_" + search + "_" + LocalDateTime.now();
        String result = pdfService.printClientList(filename, clientList);
        model.addAttribute("result", result);
        return "employee/result";
    }

    @PostMapping(path = "/export")
    public String exportSearch(@RequestParam String filename, Model model) {
        String newFilename = "csvExport/" + filename + "_" + authenticationFacade.getAuthenticatedUser().getLastname();
        String result = "File " + newFilename + ".csv generated.";

        try {
            csvService.writeCsv(newFilename, clientList);
        } catch (CsvDataTypeMismatchException e) {
            result = "Data type mismatch while writing to CSV file.";
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            result = "Required field empty while writing to CSV file.";
            e.printStackTrace();
        } catch (IOException e) {
            result = "File not found while writing to CSV file.";
            e.printStackTrace();
        }

        model.addAttribute("result", result);
        return "employee/result";
    }
}
