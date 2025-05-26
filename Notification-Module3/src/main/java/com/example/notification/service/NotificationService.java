package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.DummyAppointment;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    void processPendingNotifications();
    Optional<Notification> getNotificationById(Long id);

    // Add this method to handle appointment-based notifications
    void sendAppointmentNotification(DummyAppointment appointment);
}
