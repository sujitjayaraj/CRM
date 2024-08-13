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

    private String fullName;

    @OneToOne
    private Address mainAddress;

    @Pattern(regexp = "\\d{11}")
    private String tin;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Office> offices;
}
