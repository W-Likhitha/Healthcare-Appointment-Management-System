package com.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentRequestDTO {
	
	@NotNull(message = "Patient ID is required")
    private Long patientId;
	
	@NotNull(message = "Doctor ID is required")
    private Long doctorId;
	
	@NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;
    
   // private boolean followUp;
}

