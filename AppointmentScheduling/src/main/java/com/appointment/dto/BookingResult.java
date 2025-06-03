package com.appointment.dto;

import com.appointment.entity.Appointment;
import com.appointment.entity.WaitingAppointment;

import lombok.Data;

@Data
public class BookingResult {
    // True if an appointment was successfully booked.
    private boolean booked;
    private Appointment appointment;
    private WaitingAppointment waitingAppointment;
    private String message;
}
