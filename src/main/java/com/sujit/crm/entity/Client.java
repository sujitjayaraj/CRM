package com.sujit.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Client Name is mandatory")
    private String name;

    private String status;

    //Tax Identification Number
    @Pattern(regexp = "\\d{11}", message = "TIN must be 11 digits")
    private String tin;

    private LocalDateTime createdOn;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactPerson contactPerson;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}