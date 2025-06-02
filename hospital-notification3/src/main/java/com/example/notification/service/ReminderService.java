package com.example.notification.service;

import com.example.notification.model.Appointment;
import com.example.notification.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

@Service
public class ReminderService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TwilioSmsService smsService;

    // @Scheduled(cron = "0 0 8 * * ?") // Runs daily at 8 AM
    @Scheduled(cron = "0 36 11 * * ?") // Runs daily at 11:30 AM

    public void sendDailyReminders() {
        List<Appointment> upcomingAppointments = getTomorrowAppointments();

        for (Appointment app : upcomingAppointments) {
            String message = "Reminder: You have an appointment scheduled on " + app.getDate();

            // Send Email
            emailService.sendEmail(app.getDoctor().getEmail(), "Appointment Reminder", message);
            emailService.sendEmail(app.getPatient().getEmail(), "Appointment Reminder", message);

            // Send SMS
            smsService.sendSms(app.getDoctor().getPhoneNumber(), message);
            smsService.sendSms(app.getPatient().getPhoneNumber(), message);
        }
    }

    private List<Appointment> getTomorrowAppointments() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1); // Get tomorrow's date
        Date tomorrow = cal.getTime();
        return appointmentRepository.findByDate(tomorrow);
    }
}
