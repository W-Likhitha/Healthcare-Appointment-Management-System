package com.example.notification.model;

public class DummyAppointment {

    private Long doctorId;
    private String doctorEmail;
    private Long patientId;
    private String patientEmail;
    private String status; // BOOKED, MODIFIED, CANCELED

    public DummyAppointment(Long doctorId, String doctorEmail, Long patientId, String patientEmail, String status) {
        this.doctorId = doctorId;
        this.doctorEmail = doctorEmail;
        this.patientId = patientId;
        this.patientEmail = patientEmail;
        this.status = status;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getStatus() {
        return status;
    }
}
