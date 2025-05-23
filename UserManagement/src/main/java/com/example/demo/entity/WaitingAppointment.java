package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
//import com.example.demo.entity.User;

@Data
@Entity
@Table(name = "waiting_appointments")
public class WaitingAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
    
    @Column(name = "preferred_time")
    private LocalDateTime preferredTime;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt = LocalDateTime.now();
}
