package com.example.notification.dto;

public class DummyAppointmentDTO {
    private Long doctorId;
    private String doctorEmail;
    private Long patientId;
    private String patientEmail;
    private String status;

    public DummyAppointmentDTO() {}

    public DummyAppointmentDTO(Long doctorId, String doctorEmail, Long patientId, String patientEmail, String status) {
        this.doctorId = doctorId;
        this.doctorEmail = doctorEmail;
        this.patientId = patientId;
        this.patientEmail = patientEmail;
        this.status = status;
    }

    // Getters and Setters
    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    public String getDoctorEmail() {
        return doctorEmail;
    }
    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
