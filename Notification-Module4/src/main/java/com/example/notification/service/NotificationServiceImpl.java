package com.example.notification.service;

import com.example.notification.dao.NotificationDAO;
import com.example.notification.dto.DummyAppointmentDTO;
import com.example.notification.dto.NotificationDTO;
import com.example.notification.model.Notification;
import com.example.notification.service.sender.NotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;
    
    // Using the sender bean specifically for email notifications (assumes type "EMAIL")
    @Autowired
    private NotificationSender emailNotificationSender;

    @Override
    public Notification createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setUserId(notificationDTO.getUserId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setIsSent(notificationDTO.getIsSent() != null ? notificationDTO.getIsSent() : false);
        return notificationDAO.save(notification);
    }

    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationDAO.findById(id);
    }

    @Override
    public List<Notification> getPendingNotifications() {
        return notificationDAO.findUnsent();
    }

    @Override
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationDAO.findUnsent();
        for (Notification notification : pendingNotifications) {
            if ("EMAIL".equalsIgnoreCase(notification.getType())) {
                emailNotificationSender.sendNotification(notification);
                notification.setIsSent(true);
                notificationDAO.save(notification);
            }
        }
    }

    @Override
    public void sendAppointmentNotification(DummyAppointmentDTO appointmentDTO) {
        // Create a notification from the appointment data.
        Notification notification = new Notification();
        notification.setUserId(appointmentDTO.getPatientId());
        notification.setMessage("Appointment " + appointmentDTO.getStatus() +
            " with Dr. " + appointmentDTO.getDoctorEmail());
        notification.setType("EMAIL");
        notification = notificationDAO.save(notification);
        
        // Optionally send the notification immediately.
        emailNotificationSender.sendNotification(notification);
        notification.setIsSent(true);
        notificationDAO.save(notification);
    }
}
