package com.sujit.crm.service;

import com.sujit.crm.entity.Event;
import com.sujit.crm.entity.Notification;
import com.sujit.crm.repository.EventRepository;
import com.sujit.crm.repository.NotificationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class NotificationService {
    private EventRepository eventRepository;
    private NotificationRepository notificationRepository;
    private List<Event> todayEvents = new ArrayList<>();
    public NotificationService(EventRepository eventRepository, NotificationRepository notificationRepository) {
        super();
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
    }

    /**Checks if notification should be generated
     * any 5th minute from 9am to 5pm on weekdays
     * and generates notification if required
     */
    @Scheduled(cron = "0 */5 9-17 * * MON-FRI")
    public void checkForNotification() {

        for(Event event: todayEvents) {
            if(LocalDateTime.now().isAfter(event.getCreatedAt())) {
                List<Notification> notificationList = notificationRepository.findByEvent(event);
                notificationRepository.deleteAll(notificationList);
                todayEvents.remove(event);
            }
            else if(LocalDateTime.now().plusHours(1).isAfter(event.getCreatedAt())) {
                Notification notification = notificationRepository.findFirstByEventOrderByCreatedAtDesc(event);
                List<Notification> notificationList = notificationRepository.findByEvent(event);
                if(notificationList.size() < 3){
                    this.generateNotification(event);
                }

            }
            else if(LocalDateTime.now().plusHours(2).isAfter((event.getCreatedAt()))) {
                Notification notification = notificationRepository.findFirstByEventOrderByCreatedAtDesc(event);
                List<Notification> notificationList = notificationRepository.findByEvent(event);
                if(notificationList.size() < 2){
                    this.generateNotification(event);
                }
            }
        }
    }

    /**Generates and saves to database new Notification for given Event
     *
     * @param event Event for which notification is to be generated
     */
    private void generateNotification(Event event) {
        Notification notification = new Notification();
        notification.setEvent(event);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSeen(false);
        notification.setUser(event.getUser());
        notification.setMessage("You have " + event.getType() + " with " + event.getClient().getName() + " at " + event.getCreatedAt() + ". Topic:" + event.getName());
        notificationRepository.save(notification);
    }

    /**Gets list of today's events
     * at 8:30am on weekdays
     */
    @Scheduled(cron = "0 30 8 * * MON-FRI")
    public void generateMorningNotifications() {
        List<Event> todayEvents = eventRepository.findByTimeBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        for(Event event: todayEvents) {
            this.generateNotification(event);
        }
    }

}
