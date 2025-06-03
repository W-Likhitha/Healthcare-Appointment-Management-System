package com.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.appointment.entity.Appointment;
import com.appointment.entity.AppointmentStatus;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments by the patient’s ID
    List<Appointment> findByPatient_PatientId(Long patientId);
    
    // Find appointments by the doctor’s ID
   //  List<Appointment> findByDoctor_DoctorId(Long doctorId);
    
    // Find appointments for a given doctor on a specific date.
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);

    // Find first appointment that matches doctor, patient, date, and start time (for duplicate checking).
    Appointment findFirstByDoctorAndPatientAndAppointmentDateAndAppointmentTime(
            Doctor doctor, Patient patient, LocalDate date, LocalTime startTime);
    
    // Retrieve appointments with a specified date and status.
    List<Appointment> findByAppointmentDateAndStatus(LocalDate appointmentDate, AppointmentStatus status);
}
