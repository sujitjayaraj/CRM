package com.sujit.crm.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class ClientData {
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String status;
    @CsvBindByName
    private String tin;
    @CsvBindByName
    private String userEmail;

    @CsvBindByName
    private String contactFirstname;
    @CsvBindByName
    private String contactLastname;
    @CsvBindByName
    private String contactEmail;
    @CsvBindByName
    private String contactPhone;

    @CsvBindByName
    private String street;
    @CsvBindByName
    private String city;
    @CsvBindByName
    private String state;
    @CsvBindByName
    private String pincode;
    @CsvBindByName
    private String country;
}
