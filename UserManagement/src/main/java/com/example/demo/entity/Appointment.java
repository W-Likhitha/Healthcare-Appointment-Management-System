package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
//import com.example.demo.entity.User;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;
    
    // Separate columns for date and time
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;
    
    @Column(name = "appointment_time")
    private LocalTime appointmentTime;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    private boolean followUp;
}
