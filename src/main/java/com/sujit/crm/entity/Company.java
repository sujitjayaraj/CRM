package com.sujit.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    private Address mainAddress;

    //Tax Identification Number
    @Pattern(regexp = "\\d{11}", message = "TIN must be 11 digits")
    private String tin;

    @OneToMany(mappedBy = "address")
    private List<Office> offices;
}
