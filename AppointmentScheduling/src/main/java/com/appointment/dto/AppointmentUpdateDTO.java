package com.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class AppointmentUpdateDTO {
    // Optional: only update if provided. Fields left null won’t change.
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    //private Boolean followUp;
}
