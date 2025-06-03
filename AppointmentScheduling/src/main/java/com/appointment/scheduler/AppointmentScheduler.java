package com.appointment.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.appointment.service.AppointmentService;

@Component
public class AppointmentScheduler {

    @Autowired
    private AppointmentService appointmentService;

    // This cron expression runs at 18:00s
    @Scheduled(cron = "0 0 18 * * ?")
    public void cancelOverdueAppointmentsTask() {
        appointmentService.cancelOverdueAppointments();
    }

}
