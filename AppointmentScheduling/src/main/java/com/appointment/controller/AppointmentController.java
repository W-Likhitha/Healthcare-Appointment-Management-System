package com.appointment.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appointment.dto.AppointmentRequestDTO;
import com.appointment.dto.AppointmentResponseDTO;
import com.appointment.dto.AppointmentUpdateDTO;
import com.appointment.dto.AvailabilityRequestDTO;
import com.appointment.dto.AvailabilityResponseDTO;
import com.appointment.dto.BookingResult;
import com.appointment.entity.Appointment;
import com.appointment.service.AppointmentService;


@RestController
@RequestMapping("/profile/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    // Book an appointment (or add to waiting list if slot is full).
    @PostMapping("/book")
    public ResponseEntity<BookingResult> bookAppointment(@Valid @RequestBody AppointmentRequestDTO request) {
        java.time.LocalDateTime appointmentDateTime = request.getAppointmentDate().atTime(request.getAppointmentTime());
        BookingResult result = appointmentService.bookOrWaitAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                appointmentDateTime);
        return ResponseEntity.ok(result);
    }
    
    // Partially update an appointment.
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentUpdateDTO updateDTO) {
        Appointment appointment = appointmentService.partialUpdateAppointment(appointmentId, updateDTO);
        return ResponseEntity.ok(appointment);
    }
    
    // Cancel an appointment and reassign waiting list if applicable.
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(appointment);
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> responseDTOs = appointmentService.getAppointmentResponseDTOsByPatient(patientId);
        return ResponseEntity.ok(responseDTOs);
    }
    
     //Retrieve available time slots for a doctor on a specific date.
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponseDTO> getAvailability(@RequestBody AvailabilityRequestDTO request) {
        AvailabilityResponseDTO response = appointmentService.getAvailability(
                request.getDoctorId(),
                request.getDate());
        return ResponseEntity.ok(response);
    }
}