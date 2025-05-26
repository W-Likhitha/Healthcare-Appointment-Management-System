package com.example.notification.controller;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
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

    @Autowired
    private NotificationRepository notificationRepository;

    // ✅ Create a New Notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    // ✅ Fetch Notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Retrieve All Unsent Notifications (Fix for Your Issue)
    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findByIsSentFalse();
        return ResponseEntity.ok(pendingNotifications);
    }

    // ✅ Manually Process Pending Notifications
    @PostMapping("/process")
    public ResponseEntity<String> processPendingNotifications() {
        notificationService.processPendingNotifications();
        return ResponseEntity.ok("Pending notifications processed successfully.");
    }
}
