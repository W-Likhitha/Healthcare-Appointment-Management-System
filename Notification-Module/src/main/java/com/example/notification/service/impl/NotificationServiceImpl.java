package com.example.notification.service.impl;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.service.NotificationService;
import com.example.notification.service.sender.NotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private ApplicationContext context;

    @Override
    public Notification createNotification(Notification notification) {
        notification.setIsSent(false);  // Initially false
        return notificationRepository.save(notification);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findByIsSentFalse();
        for (Notification notification : pendingNotifications) {
            try {
                NotificationSender sender = (NotificationSender) context.getBean(notification.getType());
                sender.sendNotification(notification);
                // Update notification state after sending
                notification.setIsSent(true);
                notificationRepository.save(notification);
            } catch (Exception e) {
                System.err.println("Error sending notification " 
                        + notification.getNotificationId() + ": " + e.getMessage());
                // Optionally add retry logic here
            }
        }
    }
    
    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }
}
