package com.example.notification.controller;

import com.example.notification.model.DummyAppointment;
import com.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class DummyAppointmentController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/simulate")
    public ResponseEntity<String> triggerNotification(@RequestBody DummyAppointment appointment) {
        notificationService.sendAppointmentNotification(appointment);
        return ResponseEntity.ok("Notification triggered for " + appointment.getStatus());
    }
}
