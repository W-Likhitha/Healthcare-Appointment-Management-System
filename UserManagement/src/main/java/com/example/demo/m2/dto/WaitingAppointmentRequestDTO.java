package com.example.demo.m2.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WaitingAppointmentRequestDTO {
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Preferred time is required")
    private LocalDateTime preferredTime;
}
