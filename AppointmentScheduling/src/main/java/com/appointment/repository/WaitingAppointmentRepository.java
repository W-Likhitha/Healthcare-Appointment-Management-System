package com.appointment.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appointment.entity.WaitingAppointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
//import com.example.demo.entity.User;

@Repository
public interface WaitingAppointmentRepository extends JpaRepository<WaitingAppointment, Long> {
    List<WaitingAppointment> findByDoctorAndPreferredTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    
    // Add this method to find waiting list records for a specific doctor and patient.
    List<WaitingAppointment> findByDoctorAndPatient(Doctor doctor, Patient patient);
}
