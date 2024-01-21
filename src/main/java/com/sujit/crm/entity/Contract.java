package com.sujit.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String filename;

    private LocalDateTime createdAt;

    private boolean accepted;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "accepted_by_id")
    private User acceptedBy;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Double value;
}