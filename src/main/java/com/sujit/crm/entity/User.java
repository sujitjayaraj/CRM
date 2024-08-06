package com.sujit.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles;

    private boolean active;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "user")
    private List<Client> clients;

    public String getName() {
        return getFirstname() + " " + getLastname();
    }
}
