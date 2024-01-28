package com.sujit.crm.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sujit.crm.data.ClientData;
import com.sujit.crm.data.ClientMapper;
import com.sujit.crm.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportExportService {
    private static final String CSV_EXTENSION = ".csv";
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    private ClientService clientService;
    public List<Client> readCsvWithHeader(String filename) throws IOException {
        Reader reader;
        reader = Files.newBufferedReader(Paths.get(filename + CSV_EXTENSION));
        CsvToBean<ClientData> csvToBean = new CsvToBeanBuilder<ClientData>(reader).withType(ClientData.class).withIgnoreLeadingWhiteSpace(true).build();
        List<ClientData> clientDataList = csvToBean.parse();
        List<Client> clientList = new ArrayList<Client>();

        for (ClientData clientData : clientDataList) {
            Client client = clientMapper.mapToClient(clientData);
            clientService.saveClient(client);
            clientList.add(client);
        }

        return clientList;
    }

    public void writeCsv(String filename, List<Client> clientList) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get(filename + CSV_EXTENSION));
        StatefulBeanToCsv<ClientData> beanToCsv = new StatefulBeanToCsvBuilder<ClientData>(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        List<ClientData> clientDataList = new ArrayList<ClientData>();

        for (Client client : clientList) {
            ClientData clientData = clientMapper.mapToClientData(client);
            clientDataList.add(clientData);
        }

        beanToCsv.write(clientDataList);
        writer.close();
    }
}
