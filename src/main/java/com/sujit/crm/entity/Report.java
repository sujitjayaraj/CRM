package com.sujit.crm.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String filename;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}