package com.cr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cr.entity.Consultation;
import com.cr.service.ConsultationService;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;
    
    /**
     * Creates a new consultation for a specific appointment.
     * URL Example: POST /api/consultations/{appointmentId}
     */
    @PostMapping("/{appointmentId}")
    public ResponseEntity<Consultation> createConsultation(
            @PathVariable Long appointmentId,
            @RequestBody Consultation consultationData) {
        Consultation created = consultationService.createConsultation(appointmentId, consultationData);
        return ResponseEntity.ok(created);
    }
    
    /**
     * Retrieves the consultation record for a given appointment.
     * URL Example: GET /api/consultations/{appointmentId}
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<Consultation> getConsultation(@PathVariable Long appointmentId) {
        Consultation consultation = consultationService.getConsultationByAppointmentId(appointmentId);
        return ResponseEntity.ok(consultation);
    }
    
    /**
     * Updates an existing consultation.
     * URL Example: PUT /api/consultations/{consultationId}
     */
    @PutMapping("/{consultationId}")
    public ResponseEntity<Consultation> updateConsultation(
            @PathVariable Long consultationId,
            @RequestBody Consultation updatedData) {
        Consultation updated = consultationService.updateConsultation(consultationId, updatedData);
        return ResponseEntity.ok(updated);
    }
}
