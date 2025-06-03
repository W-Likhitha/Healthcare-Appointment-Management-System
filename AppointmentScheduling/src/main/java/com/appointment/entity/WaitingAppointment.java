package com.appointment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Doctor;

@Data
@Entity
@Table(name = "waiting_appointments")
public class WaitingAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "preferred_time")
    private LocalDateTime preferredTime;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt = LocalDateTime.now();
}
