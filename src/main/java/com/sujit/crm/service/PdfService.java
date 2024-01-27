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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdfService {

    @Autowired
    CompanyRepository companyRepository;
    private static final String TYPE = ".pdf";
    private static final List<String> TITLE_LIST = Arrays.asList("Id", "Name", "Status", "Created", "Contact name", "Contact email", "City", "State", "Responsible Employee");

    /**
     * @param filename without extension, with folder path
     * @param clientList to print
     * @return String with the result of the operation
     */
    public String printClientList(String filename, List<Client> clientList) {
        List<List<String>> data = new ArrayList<>();

        data.add(TITLE_LIST);

        for (Client client : clientList) {
            List<String> row = new ArrayList<>();
            row.add(client.getId().toString() + " ");
            row.add(client.getName() + " ");
            row.add(client.getStatus() + " ");
            row.add(client.getCreatedOn().toString() + " ");
            row.add(client.getContactPerson().getName() + " ");
            row.add(client.getContactPerson().getEmail() + " ");
            row.add(client.getAddress().getCity() + " ");
            row.add(client.getAddress().getState() + " ");
            row.add(client.getUser().getFullName() + " ");

            data.add(row);
        }

        try {
            makeTablePdf(filename, data);
            return "Pdf " + filename + " printed";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Pdf " + filename + " not printed";
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

        for (List<String> datum : data) {
            for (String s : datum) {
                table.addCell(new Cell().add(new Paragraph(s)));
                System.out.println(s);
            }
        }
        document.add(table);
        document.close();
    }
    public void printContract(Contract contract) throws FileNotFoundException {
        String filename = contract.getFilename() + TYPE;
        File file = new File(filename);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(contract.getName()));
        document.add(new Paragraph(contract.getClient().getName()));
        document.add(new Paragraph(contract.getClient().getAddress().getCity()));
        document.add(new Paragraph(contract.getClient().getAddress().getStreet()));
        document.add(new Paragraph(contract.getClient().getAddress().getPincode()));
        document.add(new Paragraph(contract.getClient().getTin()));
        document.add(new Paragraph(""));

        Company ourCompany = companyRepository.findAll().getFirst();
        document.add(new Paragraph(ourCompany.getName()));
        document.add(new Paragraph(ourCompany.getMainAddress().getCity()));
        document.add(new Paragraph(ourCompany.getMainAddress().getStreet()));
        document.add(new Paragraph(ourCompany.getMainAddress().getPincode()));
        document.add(new Paragraph(ourCompany.getTin()));
        document.add(new Paragraph(""));

        document.add(new Paragraph("Test contract with value "+contract.getValue().toString()));
        StringBuilder sb = new StringBuilder();
        sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(10));
        document.add(new Paragraph(sb.toString()));
        document.add(new Paragraph(sb.toString()));
        document.add(new Paragraph(sb.toString()));
        document.add(new Paragraph(sb.toString()));

        document.add(new Paragraph(""));
        document.add(new Paragraph(LocalDate.now().toString()));

        document.close();
    }
}