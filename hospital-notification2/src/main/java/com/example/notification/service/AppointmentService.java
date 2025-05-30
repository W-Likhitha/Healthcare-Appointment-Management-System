package com.example.notification.service;

import com.example.notification.model.Appointment;
import com.example.notification.model.User;
import com.example.notification.repository.AppointmentRepository;
import com.example.notification.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public Appointment createAppointment(Long doctorId, Long patientId, Date date) {
        User doctor = userRepository.findById(doctorId)
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User patient = userRepository.findById(patientId)
                        .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDate(date);
        appointment.setStatus("CREATED");

        Appointment saved = appointmentRepository.save(appointment);
        notifyUsers(saved, "Appointment Created");
        return saved;
    }

    public Appointment modifyAppointment(Long id, Date updatedDate) {
        return appointmentRepository.findById(id).map(app -> {
            app.setDate(updatedDate);
            app.setStatus("MODIFIED");
            Appointment saved = appointmentRepository.save(app);
            notifyUsers(saved, "Appointment Modified");
            return saved;
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.findById(id).ifPresent(app -> {
            app.setStatus("CANCELLED");
            appointmentRepository.save(app);
            notifyUsers(app, "Appointment Cancelled");
        });
    }

    private void notifyUsers(Appointment app, String subject) {
        emailService.sendEmail(app.getDoctor().getEmail(), subject, app.toString());
        emailService.sendEmail(app.getPatient().getEmail(), subject, app.toString());
    }
}
