package com.example.notification.service;

import com.example.notification.dto.NotificationDTO;
import com.example.notification.dto.DummyAppointmentDTO;
import com.example.notification.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(NotificationDTO notificationDTO);
    Optional<Notification> getNotificationById(Long id);
    List<Notification> getPendingNotifications();
    void processPendingNotifications();
    void sendAppointmentNotification(DummyAppointmentDTO appointmentDTO);
}
