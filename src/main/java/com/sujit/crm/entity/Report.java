package com.sujit.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String filename;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
