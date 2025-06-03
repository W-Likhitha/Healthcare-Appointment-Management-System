package com.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class AvailabilityResponseDTO {
    private Long doctorId;
    private LocalDate date;
    private List<LocalTime> availableSlots;
}
