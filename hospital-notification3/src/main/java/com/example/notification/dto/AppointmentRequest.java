package com.example.notification.dto;

import java.util.Date;

public class AppointmentRequest {
    private Long doctorId;
    private Long patientId;
    private Date date;

    public Long getDoctorId() { return doctorId; }
    public Long getPatientId() { return patientId; }
    public Date getDate() { return date; }

    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setDate(Date date) { this.date = date; }
}
