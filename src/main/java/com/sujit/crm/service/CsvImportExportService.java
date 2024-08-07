package com.sujit.crm.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sujit.crm.dto.ClientDto;
import com.sujit.crm.dto.ClientMapper;
import com.sujit.crm.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvImportExportService {

    private static final String TYPE = ".csv";
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    private ClientService clientService;

    /**
     * Imports given CSV file with {@link java.util.List} of {@link com.sujit.crm.entity.Client} objects.
     *
     * @param filename Absolute path of the file to be imported without the extension.
     * @return {@link java.util.List} of {@link com.sujit.crm.entity.Client} objects.
     * @throws IOException if file is not found.
     */
    public List<Client> readCsvWithHeader(String filename) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filename + TYPE));
        CsvToBean<ClientDto> csvToBean = new CsvToBeanBuilder<ClientDto>(reader)
                .withType(ClientDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<ClientDto> clientDtoList = csvToBean.parse();
        List<Client> clientList = new ArrayList<>();

        for (ClientDto clientDto: clientDtoList) {
            Client client = clientMapper.toEntity(clientDto);
            clientService.saveClient(client);
            clientList.add(client);
        }

        return clientList;
    }

    /**
     * Exports given {@link java.util.List} of {@link com.sujit.crm.entity.Client} objects to a CSV file.
     * @param filename Absolute path of the file to be created without the extension.
     * @param clientList {@link java.util.List} of {@link com.sujit.crm.entity.Client} objects.
     * @throws CsvDataTypeMismatchException if any data type is mismatched.
     * @throws CsvRequiredFieldEmptyException if any required field is empty.
     * @throws IOException if file is not found.
     */
    public void writeCsv(String filename, List<Client> clientList) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = Files.newBufferedWriter(Paths.get(filename + TYPE));
        StatefulBeanToCsv<ClientDto> beanToCsv = new StatefulBeanToCsvBuilder<ClientDto>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        List<ClientDto> clientDtoList = new ArrayList<>();

        for (Client client: clientList) {
            clientDtoList.add(clientMapper.toDto(client));
        }

        beanToCsv.write(clientDtoList);
        writer.close();
    }
}
