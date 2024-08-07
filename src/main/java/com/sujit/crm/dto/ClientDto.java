package com.sujit.crm.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class ClientDto {

    @CsvBindByName
    private String name;
    @CsvBindByName
    private String status;
    @CsvBindByName
    private String tin;
    @CsvBindByName
    private String contactFirstName;
    @CsvBindByName
    private String contactLastName;
    @CsvBindByName
    private String contactEmail;
    @CsvBindByName
    private String contactPhone;
    @CsvBindByName
    private String country;
    @CsvBindByName
    private String state;
    @CsvBindByName
    private String city;
    @CsvBindByName
    private String street;
    @CsvBindByName
    private String pincode;
    @CsvBindByName
    private String userEmail;
}
