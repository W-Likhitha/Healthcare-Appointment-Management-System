package com.example.notification.service.sender;

import com.example.notification.model.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification);
}
