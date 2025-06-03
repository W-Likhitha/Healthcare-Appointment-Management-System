package com.appointment.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class AvailabilityRequestDTO {
    private Long doctorId;
    private LocalDate date;
}



