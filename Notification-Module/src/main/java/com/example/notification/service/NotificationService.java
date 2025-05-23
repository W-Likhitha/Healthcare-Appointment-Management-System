package com.example.notification.service;

import com.example.notification.model.Notification;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    void processPendingNotifications();
    Optional<Notification> getNotificationById(Long id);
}
