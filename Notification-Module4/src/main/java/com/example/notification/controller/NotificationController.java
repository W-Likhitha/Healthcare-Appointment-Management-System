package com.example.notification.controller;

import com.example.notification.dto.NotificationDTO;
import com.example.notification.model.Notification;
import com.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Create a new notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Notification savedNotification = notificationService.createNotification(notificationDTO);
        return ResponseEntity.ok(savedNotification);
    }

    // Fetch notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Retrieve all unsent notifications
    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications() {
        List<Notification> pendingNotifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(pendingNotifications);
    }

    // Manually process pending notifications
    @PostMapping("/process")
    public ResponseEntity<String> processPendingNotifications() {
        notificationService.processPendingNotifications();
        return ResponseEntity.ok("Pending notifications processed successfully.");
    }
}
