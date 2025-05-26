package com.appschl.controller;

//import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appschl.dto.AppointmentRequestDTO;
import com.appschl.dto.AppointmentUpdateDTO;
import com.appschl.dto.AvailabilityRequestDTO;
import com.appschl.dto.AvailabilityResponseDTO;
//import com.appschl.dto.AvailabilityRequestDTO;
import com.appschl.dto.BookingResult;
import com.appschl.entity.Appointment;
import com.appschl.service.AppointmentService;
//import com.example.demo.entity.User;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    // POST endpoint to book an appointment (or add to waiting list if slot is full).
    @PostMapping("/book")
    public ResponseEntity<BookingResult> bookAppointment(@Valid @RequestBody AppointmentRequestDTO request) {
        java.time.LocalDateTime appointmentDateTime = request.getAppointmentDate().atTime(request.getAppointmentTime());
        BookingResult result = appointmentService.bookOrWaitAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                appointmentDateTime,
                request.isFollowUp());
        return ResponseEntity.ok(result);
    }
    
    // PUT endpoint to partially update an appointment.
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentUpdateDTO updateDTO) {
        Appointment appointment = appointmentService.partialUpdateAppointment(appointmentId, updateDTO);
        return ResponseEntity.ok(appointment);
    }
    
    // PUT endpoint to cancel an appointment and reassign waiting list if applicable.
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(appointment);
    }
    
    // GET endpoint to retrieve appointments by patient user ID.
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }
    
    // GET endpoint to retrieve appointments by doctor user ID.
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }
    
     //GET endpoint to retrieve available time slots for a doctor on a specific date.
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponseDTO> getAvailability(@RequestBody AvailabilityRequestDTO request) {
        AvailabilityResponseDTO response = appointmentService.getAvailability(
                request.getDoctorId(),
                request.getDate());
        return ResponseEntity.ok(response);
    }




}