package com.example.notification.service.impl;

import com.example.notification.model.Notification;
import com.example.notification.model.DummyAppointment;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.service.NotificationService;
import com.example.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Notification createNotification(Notification notification) {
        notification.setIsSent(false);  // Initially set to false
        return notificationRepository.save(notification);
    }

    @Override
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findByIsSentFalse();
        for (Notification notification : pendingNotifications) {
            try {
                System.out.println("Sending notification: " + notification.getMessage());

                // Send email notification
                emailService.sendEmail("recipient@example.com", "New Notification", notification.getMessage());

                notification.setIsSent(true);
                notificationRepository.save(notification);
            } catch (Exception e) {
                System.err.println("Error sending notification " + notification.getNotificationId() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void sendAppointmentNotification(DummyAppointment appointment) {
        String message;

        switch (appointment.getStatus()) {
            case "BOOKED":
                message = "Your appointment has been successfully booked.";
                break;
            case "MODIFIED":
                message = "Your appointment has been modified.";
                break;
            case "CANCELED":
                message = "Your appointment has been canceled.";
                break;
            default:
                message = "Unknown appointment status.";
        }

        // Create notifications for both patient and doctor
        Notification patientNotification = new Notification();
        patientNotification.setUserId(appointment.getPatientId());
        patientNotification.setMessage(message);
        patientNotification.setType("EMAIL");

        Notification doctorNotification = new Notification();
        doctorNotification.setUserId(appointment.getDoctorId());
        doctorNotification.setMessage(message);
        doctorNotification.setType("EMAIL");

        // Save notifications
        notificationRepository.save(patientNotification);
        notificationRepository.save(doctorNotification);

        // Send email to doctor and patient using emails from the request
        emailService.sendEmail(appointment.getDoctorEmail(), "Appointment Update", message);
        emailService.sendEmail(appointment.getPatientEmail(), "Appointment Update", message);
    }
}
