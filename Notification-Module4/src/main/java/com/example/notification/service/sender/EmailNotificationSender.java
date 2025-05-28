package com.example.notification.service.sender;

import com.example.notification.model.Notification;
import org.springframework.stereotype.Component;

@Component("EMAIL")
public class EmailNotificationSender implements NotificationSender {
    @Override
    public void sendNotification(Notification notification) {
        // This is where you would integrate with an actual email service.
        System.out.println("Email sent to User " + notification.getUserId() +
                " with message: " + notification.getMessage());
    }
}
