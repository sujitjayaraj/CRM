package com.sujit.crm.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Company;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    CompanyRepository companyRepository;

    private static final String TYPE = ".pdf";
    private static final List<String> TITLE_LIST = Arrays.asList("Id", "Name", "Status", "Created", "Contact name", "Contact email", "City", "Region", "Responsible employee");

    /**
     * Sends give {@link java.util.List} of {@link com.sujit.crm.entity.Client} to generate a PDF file.
     *
     * @param filename Absolute path of the file to be created without the extension.
     * @param clientList List of clients to be included in the PDF.
     * @return {@link String} result of action.
     */
    public String printClientList(String filename, List<Client> clientList) {
        List<List<String>> data = new ArrayList<>();
        data.add(TITLE_LIST);

        for (Client client : clientList) {
            List<String> row = new ArrayList<>();
            row.add(client.getId() + " ");
            row.add(client.getName() + " ");
            row.add(client.getStatus() + " ");
            row.add(client.getCreated() + " ");
            row.add(client.getContactPerson().getName() + " ");
            row.add(client.getContactPerson().getEmail() + " ");
            row.add(client.getAddress().getCity() + " ");
            row.add(client.getAddress().getState() + " ");
            row.add(client.getUser().getName() + " ");
            data.add(row);
        }

        try {
            makeTablePdf(filename, data);
            return "PDF " + filename + " generated";
        } catch (IOException e) {
            return "Error generating PDF " + filename;
        }
    }

    private void makeTablePdf(String filename, List<List<String>> data) throws IOException {
        File file = new File(filename + TYPE);
        file.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(filename + TYPE);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph(filename));
        Table table = new Table(data.getFirst().size());

        for (List<String> record: data) {
            for (String field: record) {
                Cell cell = new Cell().add(new Paragraph(field));
                table.addCell(cell);
                System.out.println(field);
            }
        }

        document.add(table);
        document.close();
    }

    /**
     * Generates a PDF contract file for given {@link com.sujit.crm.entity.Contract}.
     *
     * @param contract Given {@link com.sujit.crm.entity.Contract} object.
     * @throws FileNotFoundException if the file is not found.
     */
    public void printContract(Contract contract) throws FileNotFoundException {
        String filename = contract.getFilename() + TYPE;
        File file = new File(filename);
        file.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph(contract.getTitle()));
        document.add(new Paragraph(contract.getClient().getName()));
        document.add(new Paragraph(contract.getClient().getAddress().getCity()));
        document.add(new Paragraph(contract.getClient().getAddress().getState()));
        document.add(new Paragraph(contract.getClient().getAddress().getPincode()));
        document.add(new Paragraph(contract.getClient().getTin()));
        document.add(new Paragraph(""));

        Company ourCompany = companyRepository.findAll().getFirst();
        document.add(new Paragraph(ourCompany.getFullName()));
        document.add(new Paragraph(ourCompany.getMainAddress().getCity()));
        document.add(new Paragraph(ourCompany.getMainAddress().getStreet()));
        document.add(new Paragraph(ourCompany.getMainAddress().getPincode()));
        document.add(new Paragraph(ourCompany.getTin()));
        document.add(new Paragraph(""));
        document.add(new Paragraph("Test contract with value " + contract.getValue().toString()));
        document.add(new Paragraph(" test ".repeat(50)));
        document.add(new Paragraph(" test ".repeat(50)));
        document.add(new Paragraph(" test ".repeat(50)));
        document.add(new Paragraph(" test ".repeat(50)));
        document.add(new Paragraph(""));
        document.add(new Paragraph(LocalDateTime.now().toString()));
        document.close();
    }
}
