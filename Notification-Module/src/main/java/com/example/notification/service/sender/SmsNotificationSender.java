package com.example.notification.service.sender;

import com.example.notification.model.Notification;
import org.springframework.stereotype.Component;

@Component("SMS") // Bean name must match the notification type.
public class SmsNotificationSender implements NotificationSender {
    @Override
    public void sendNotification(Notification notification) {
        // Integrate with an SMS service (like Twilio) here.
        System.out.println("SMS sent to User " + notification.getUserId() 
            + " with message: " + notification.getMessage());
    }
}
