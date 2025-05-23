package com.example.notification.service.sender;

import com.example.notification.model.Notification;
import org.springframework.stereotype.Component;

@Component("EMAIL") // Bean name must match the notification type.
public class EmailNotificationSender implements NotificationSender {
    @Override
    public void sendNotification(Notification notification) {
        // Integrate with an email service (like SendGrid) here.
        System.out.println("Email sent to User " + notification.getUserId() 
            + " with message: " + notification.getMessage());
    }
}
