package com.cr.service;

import com.cr.entity.Consultation;
import com.cr.exception.ConsultationNotFoundException;
import com.cr.repository.ConsultationRepository;
import com.appschl.entity.Appointment;
import com.appschl.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public Consultation createConsultation(Long appointmentId, Consultation consultationData) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found with id " + appointmentId));
        
        // Ensure only one consultation per appointment
        if (consultationRepository.findByAppointment(appointment) != null) {
            throw new RuntimeException("A consultation record already exists for this appointment.");
        }
        
        consultationData.setAppointment(appointment);
        return consultationRepository.saveAndFlush(consultationData);
    }
    
    public Consultation getConsultationByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ConsultationNotFoundException("Appointment not found with id " + appointmentId));
        
        Consultation consultation = consultationRepository.findByAppointment(appointment);
        if (consultation == null) {
            // Throw our custom exception if consultation not found
            throw new ConsultationNotFoundException("Consultation not found for appointment id " + appointmentId);
        }
        return consultation;
    }
    
    public Consultation updateConsultation(Long consultationId, Consultation updatedData) {
        Consultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.setNotes(updatedData.getNotes());
        consultation.setPrescription(updatedData.getPrescription());
        return consultationRepository.saveAndFlush(consultation);
    }
}
