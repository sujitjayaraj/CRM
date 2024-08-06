package com.sujit.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private String title;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime time;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
    private List<Notification> notifications;
}
