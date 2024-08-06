package com.sujit.crm.repository;

import com.sujit.crm.entity.Event;
import com.sujit.crm.entity.Notification;
import com.sujit.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findFirstByEventOrderByCreatedDesc(Event event);
    List<Notification> findByEvent(Event event);
    List<Notification> findByUser(User user);
}
