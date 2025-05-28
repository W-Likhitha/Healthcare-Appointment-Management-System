package com.appschl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appschl.entity.Appointment;
import com.appschl.entity.AppointmentStatus;
import com.example.demo.entity.User;
import com.appschl.entity.WaitingAppointment;
import com.example.demo.entity.Role;
import com.appschl.dto.AppointmentUpdateDTO;
import com.appschl.dto.AvailabilityResponseDTO;
import com.appschl.dto.BookingResult;
import com.appschl.repository.AppointmentRepository;
import com.example.demo.repository.UserRepository;
import com.appschl.repository.WaitingAppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WaitingAppointmentRepository waitingAppointmentRepository;
    
    // Hospital working hours and breaks.
    // Each appointment is 30 minutes long and must be within 9:30 - 18:00. (Last valid start time is 17:30).
    private final LocalTime WORK_START = LocalTime.of(9, 30);
    private final LocalTime WORK_END   = LocalTime.of(18, 0);
    private final LocalTime LUNCH_START = LocalTime.of(13, 0);
    private final LocalTime LUNCH_END   = LocalTime.of(14, 30);
    private final LocalTime FOLLOWUP_ALLOWED_START = LocalTime.of(16, 0);
    
    // --- Utility Validation Methods ---
    
    // Validate that a 30-minute slot fits completely within hospital hours.
    private void validateHospitalHours(LocalTime startTime) {
        if (startTime.isBefore(WORK_START) || startTime.plusMinutes(30).isAfter(WORK_END)) {
            throw new RuntimeException("Appointment must be scheduled between 9:30 and 17:30 so that it finishes by 18:00.");
        }
    }
    // For regular (non-follow-up) appointments:
    //  • Must not overlap the lunch break (13:00 to 14:30).
    //  • Must start before 16:00.
    private void validateRegularAppointment(LocalTime startTime) {
        if (startTime.isBefore(LUNCH_END) && startTime.plusMinutes(30).isAfter(LUNCH_START)) {
            throw new RuntimeException("Regular appointments cannot overlap the lunch break (13:00 to 14:30).");
        }
        if (!startTime.isBefore(FOLLOWUP_ALLOWED_START)) {
            throw new RuntimeException("Regular appointments are not allowed between 16:00 and 18:00. For that period, select follow-up.");
        }
    }
    
    // For follow-up appointments:
    //  • Must start at or after 16:00.
    //  • Must finish by 18:00.
    private void validateFollowUpAppointment(LocalTime startTime) {
        if (startTime.isBefore(FOLLOWUP_ALLOWED_START) || startTime.plusMinutes(30).isAfter(WORK_END)) {
            throw new RuntimeException("Follow-up appointments must be scheduled between 16:00 and 18:00.");
        }
    }
    
//booking methods
    @Transactional
    public BookingResult bookOrWaitAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDateTime, boolean followUp) {
    	if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book an appointment in the past.");
        }
        BookingResult result = new BookingResult();
        
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        if (!patient.getRole().equals(Role.PATIENT)) {
            throw new RuntimeException("The provided patientId does not belong to a patient.");
        }
        if (!doctor.getRole().equals(Role.DOCTOR)) {
            throw new RuntimeException("The provided doctorId does not belong to a doctor.");
        }
        
        LocalDate date = appointmentDateTime.toLocalDate();
        LocalTime startTime = appointmentDateTime.toLocalTime();

        // ----- Duplicate Check -----
     // Check if there is already a booked appointment with the same doctor, patient, date, and time.
        Appointment duplicateAppointment = appointmentRepository
                .findFirstByDoctorAndPatientAndAppointmentDateAndAppointmentTime(doctor, patient, date, startTime);
        if (duplicateAppointment != null) {
            throw new RuntimeException("Duplicate booking: an appointment for this slot already exists.");
        }

        // Check if the patient is already on the waiting list for the same doctor and slot.
        List<WaitingAppointment> existingWaiting = waitingAppointmentRepository.findByDoctorAndPatient(doctor, patient);
        if (existingWaiting != null && !existingWaiting.isEmpty()) {
            boolean duplicateWaiting = existingWaiting.stream().anyMatch(wait ->
                wait.getPreferredTime().toLocalDate().equals(date) &&
                wait.getPreferredTime().toLocalTime().equals(startTime));
            if (duplicateWaiting) {
                throw new RuntimeException("Duplicate waiting: You are already on the waiting list for this slot.");
            }
        }
        
        // ----- Validate appointment timing -----
        validateHospitalHours(startTime);
        if (followUp) {
            validateFollowUpAppointment(startTime);
        } else {
            validateRegularAppointment(startTime);
        }
        
        LocalTime endTime = startTime.plusMinutes(30);
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndAppointmentDate(doctor, date);
        boolean slotAvailable = true;
        for (Appointment a : existingAppointments) {
            LocalTime exStart = a.getAppointmentTime();
            LocalTime exEnd = exStart.plusMinutes(30);
            if (startTime.isBefore(exEnd) && exStart.isBefore(endTime)) {
                slotAvailable = false;
                break;
            }
        }
        if (slotAvailable) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(date);
            appointment.setAppointmentTime(startTime);
            appointment.setStatus(AppointmentStatus.BOOKED);
            appointment.setFollowUp(followUp);
            Appointment saved = appointmentRepository.saveAndFlush(appointment);
            result.setBooked(true);
            result.setAppointment(saved);
            result.setMessage("Appointment booked successfully.");
            // Remove any waiting records for this patient and doctor (if any exist).
            List<WaitingAppointment> waitingRecords = waitingAppointmentRepository.findByDoctorAndPatient(doctor, patient);
            if (waitingRecords != null && !waitingRecords.isEmpty()) {
                waitingAppointmentRepository.deleteAll(waitingRecords);
                waitingAppointmentRepository.flush();
            }
        } else {
            // Slot unavailable: add patient to waiting list.
            WaitingAppointment waiting = addToWaitingList(patientId, doctorId, appointmentDateTime);
            result.setBooked(false);
            result.setWaitingAppointment(waiting);
            result.setMessage("Requested slot is full. You have been added to the waiting list automatically.");
        }
        
        return result;
    }

    @Transactional
    public Appointment partialUpdateAppointment(Long appointmentId, AppointmentUpdateDTO updateDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (updateDTO.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateDTO.getAppointmentDate());
        }
        if (updateDTO.getAppointmentTime() != null) {
            appointment.setAppointmentTime(updateDTO.getAppointmentTime());
        }
        if (updateDTO.getFollowUp() != null) {
            appointment.setFollowUp(updateDTO.getFollowUp());
        }
        
        return appointmentRepository.saveAndFlush(appointment);
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_UserId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_UserId(doctorId);
    }
    
    @Transactional
    public WaitingAppointment addToWaitingList(Long patientId, Long doctorId, LocalDateTime preferredTime) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        if (!patient.getRole().equals(Role.PATIENT)) {
            throw new RuntimeException("The provided patientId does not belong to a patient.");
        }
        if (!doctor.getRole().equals(Role.DOCTOR)) {
            throw new RuntimeException("The provided doctorId does not belong to a doctor.");
        }
        
        WaitingAppointment waiting = new WaitingAppointment();
        waiting.setPatient(patient);
        waiting.setDoctor(doctor);
        waiting.setPreferredTime(preferredTime);
        
        return waitingAppointmentRepository.saveAndFlush(waiting);
    }
    
    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        // Fetch appointment to be canceled.
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Build the appointment's start DateTime and define a ±15-minute window.
        LocalDateTime apptDateTime = appointment.getAppointmentDate().atTime(appointment.getAppointmentTime());
        LocalDateTime startWindow = apptDateTime.minusMinutes(15);
        LocalDateTime endWindow = apptDateTime.plusMinutes(15);

        // Look for waiting appointments for the same doctor within this window.
        List<WaitingAppointment> waitingList = waitingAppointmentRepository
                .findByDoctorAndPreferredTimeBetween(appointment.getDoctor(), startWindow, endWindow);
        
        // Process waiting list (if found) or cancel the appointment.
        if (waitingList != null && !waitingList.isEmpty()) {
            WaitingAppointment waiting = waitingList.get(0);
            // Reassign the appointment to the waiting patient.
            appointment.setPatient(waiting.getPatient());
            // Mark appointment as BOOKED.
            appointment.setStatus(AppointmentStatus.BOOKED);
        } else {
            appointment.setStatus(AppointmentStatus.CANCELLED);
        }

        // Save and flush the appointment update, and directly assign the result to updatedAppointment.
        Appointment updatedAppointment = appointmentRepository.save(appointment);

// If waiting records were present, delete them.
       if (waitingList != null && !waitingList.isEmpty()) {
            waitingAppointmentRepository.deleteAll(waitingList);
            waitingAppointmentRepository.flush();
        }
        return updatedAppointment;
    }

    public List<LocalTime> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        List<LocalTime> slots = new ArrayList<>();
        LocalTime slotTime = WORK_START;
        while (!slotTime.plusMinutes(30).isAfter(WORK_END)) {
            // Exclude slots overlapping lunch.
            if (!(slotTime.isBefore(LUNCH_END) && slotTime.plusMinutes(30).isAfter(LUNCH_START))) {
                slots.add(slotTime);
            }
            slotTime = slotTime.plusMinutes(30);
        }
        
        List<Appointment> appointments = appointmentRepository.findByDoctorAndAppointmentDate(doctor, date);
        for (Appointment a : appointments) {
            if (a.getStatus() == AppointmentStatus.BOOKED) {
                slots.remove(a.getAppointmentTime());
            }
        }
        
        return slots;
    }
    
    public AvailabilityResponseDTO getAvailability(Long doctorId, LocalDate date) {
        AvailabilityResponseDTO response = new AvailabilityResponseDTO();
        response.setDoctorId(doctorId);
        response.setDate(date);
        response.setAvailableSlots(getAvailableTimeSlots(doctorId, date));
        return response;
    }
    
    @Transactional
    public void cancelOverdueAppointments() {
        // Get today's date
        LocalDate today = LocalDate.now();
        // Current system time (you may adjust this if you are using a specific timezone)
        LocalTime currentTime = LocalTime.now();

        // Retrieve all appointments for today that are in BOOKED status.
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateAndStatus(today, AppointmentStatus.BOOKED);

        // Process each appointment
        for (Appointment appointment : appointments) {
            // Only cancel if the appointment time is before the current time.
            if (appointment.getAppointmentTime().isBefore(currentTime)) {
                // Mark the appointment as CANCELLED
                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointmentRepository.save(appointment);

                // Build the appointment date-time and define a ±15-minute window.
                LocalDateTime appointmentDateTime = appointment.getAppointmentDate().atTime(appointment.getAppointmentTime());
                LocalDateTime startWindow = appointmentDateTime.minusMinutes(15);
                LocalDateTime endWindow = appointmentDateTime.plusMinutes(15);

                // Find waiting appointments for the same doctor in the defined time window.
                List<WaitingAppointment> waitingList = waitingAppointmentRepository
                        .findByDoctorAndPreferredTimeBetween(appointment.getDoctor(), startWindow, endWindow);

                // Delete the waiting appointments if found.
                if (waitingList != null && !waitingList.isEmpty()) {
                    waitingAppointmentRepository.deleteAll(waitingList);
                    waitingAppointmentRepository.flush();
                }
            }
        }
    }
    
}