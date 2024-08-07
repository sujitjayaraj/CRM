package com.sujit.crm.service;

import com.sujit.crm.entity.Event;
import com.sujit.crm.entity.Notification;
import com.sujit.crm.repository.EventRepository;
import com.sujit.crm.repository.NotificationRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
public class NotificationService {
    private EventRepository eventRepository;
    private NotificationRepository notificationRepository;
    private List<Event> todayEventList;

    public NotificationService(EventRepository eventRepository, NotificationRepository notificationRepository) {
        super();
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
        this.todayEventList = new ArrayList<>();
    }

    /**
     * Checks if notifications should be generated
     * every 5 minutes 9am to 5pm Monday to Friday
     * and generates them.
     */
    @Scheduled(cron = "0 */5 9-17 * * MON-FRI")
    public void checkIfGenerateNotification() {
        for (Event event: todayEventList) {
            if (LocalDateTime.now().isAfter(event.getTime())) {
                List<Notification> list = notificationRepository.findByEvent(event);
                notificationRepository.deleteAll(list);
                todayEventList.remove(event);
            } else if (LocalDateTime.now().plusHours(1).isAfter(event.getTime())) {
                Notification notification = notificationRepository.findFirstByEventOrderByCreatedDesc(event);
                List<Notification> notificationList = notificationRepository.findByEvent(event);

                if(notificationList.size() < 3) {
                    this.generateNotification(event);
                }
            } else if (LocalDateTime.now().plusHours(2).isAfter(event.getTime())) {
                Notification notification = notificationRepository.findFirstByEventOrderByCreatedDesc(event);
                List<Notification> notificationList = notificationRepository.findByEvent(event);

                if (notificationList.size() < 2) {
                    this.generateNotification(event);
                }
            }
        }
    }

    /**
     * Gets list of today's events at 8.30am from Monday to Friday
     * and generates first notifications.
     */
    public void generateMorningNotifications() {
        todayEventList = eventRepository.findByTimeBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        for (Event event: todayEventList) {
            this.generateNotification(event);
        }
    }

    /**
     * Generates notification for the given {@link com.sujit.crm.entity.Event} and saves it to database.
     * @param event the {@link com.sujit.crm.entity.Event} for which the notification is to be generated.
     */
    private void generateNotification(Event event) {
        Notification notification = new Notification();
        notification.setEvent(event);
        notification.setCreated(LocalDateTime.now());
        notification.setWasRead(false);
        notification.setUser(event.getUser());
        notification.setContent("You have " + event.getType() + " with " + event.getClient().getName() + " at " + event.getTime() + ". Topic: " + event.getTitle());
        notificationRepository.save(notification);
    }
}
