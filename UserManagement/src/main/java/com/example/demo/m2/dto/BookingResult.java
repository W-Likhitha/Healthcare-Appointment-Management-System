package com.example.demo.m2.dto;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.WaitingAppointment;
import lombok.Data;

@Data
public class BookingResult {
    // True if an appointment was successfully booked.
    private boolean booked;
    private Appointment appointment;
    private WaitingAppointment waitingAppointment;
    private String message;
}
