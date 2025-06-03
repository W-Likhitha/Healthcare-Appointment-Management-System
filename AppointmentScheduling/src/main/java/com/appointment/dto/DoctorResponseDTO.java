package com.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long doctorId;
    private String name;
    private String gender;
    private String roomNumber;
    private String specialization;
    private String qualification;
    // Phone and email fields are omitted intentionally.
}
