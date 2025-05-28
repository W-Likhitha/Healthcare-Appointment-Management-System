package com.cr.entity;

import com.appschl.entity.Appointment;
import jakarta.persistence.*;
import lombok.*;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "consultation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;  // Primary key for the consultation record

    // Associates the consultation record with a specific appointment.
    @OneToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Consultation notes (up to 2000 characters).
    @Column(length = 2000)
    private String notes;

    // Prescription details stored as a hash map.
    @ElementCollection
    @CollectionTable(name = "consultation_prescriptions", joinColumns = @JoinColumn(name = "consultation_id"))
    @MapKeyColumn(name = "medicine_name")
    @Column(name = "dosage", length = 1000)
    private Map<String, String> prescription = new HashMap<>();
}
