package com.sujit.crm.api;

import com.sujit.crm.entity.Client;
import com.sujit.crm.service.CsvImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/import")
public class ImportResource {
    @Autowired
    private CsvImportExportService csvService;

    @GetMapping
    public String importCsvView() {
        return "import/getFilename";
    }

    @PostMapping
    public String importCsv(@RequestParam String filename, Model model) {
        String newFilename = "csvImport/" + filename;
        String result = "File imported successfully";

        try {
            List<Client> clientList = csvService.readCsvWithHeader(newFilename);
            model.addAttribute("clientList", clientList);
        } catch (IOException e) {
            result = "Error occured while importing file";
            e.printStackTrace();
        }

        model.addAttribute("result", result);
        return "import/show";
    }
}
