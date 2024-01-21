package com.sujit.crm.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    private boolean seen;

    private String message;
}
