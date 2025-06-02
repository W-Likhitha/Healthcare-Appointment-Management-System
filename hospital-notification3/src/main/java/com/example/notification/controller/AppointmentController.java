package com.example.notification.controller;

import com.example.notification.dto.AppointmentRequest;
import com.example.notification.model.Appointment;
import com.example.notification.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService service;

    @PostMapping
    public Appointment create(@RequestBody AppointmentRequest request) {
        return service.createAppointment(request.getDoctorId(), request.getPatientId(), request.getDate());
    }

    @PutMapping("/{id}")
    public Appointment modify(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        return service.modifyAppointment(id, request.getDate());
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancelAppointment(id);
    }
}
