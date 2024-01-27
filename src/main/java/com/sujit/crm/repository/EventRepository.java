package com.sujit.crm.repository;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTimeAfter(LocalDateTime dateTime);
    List<Event> findByTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Event> findByClient(Client client);
}
