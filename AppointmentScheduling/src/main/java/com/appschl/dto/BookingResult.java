package com.appschl.dto;

import com.appschl.entity.Appointment;
import com.appschl.entity.WaitingAppointment;

import lombok.Data;

@Data
public class BookingResult {
    // True if an appointment was successfully booked.
    private boolean booked;
    private Appointment appointment;
    private WaitingAppointment waitingAppointment;
    private String message;
}
