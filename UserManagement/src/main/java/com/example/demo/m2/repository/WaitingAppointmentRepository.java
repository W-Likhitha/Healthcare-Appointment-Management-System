package com.example.demo.m2.repository;

import com.example.demo.entity.WaitingAppointment;
import com.example.demo.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingAppointmentRepository extends JpaRepository<WaitingAppointment, Long> {
    List<WaitingAppointment> findByDoctorAndPreferredTimeBetween(User doctor, LocalDateTime start, LocalDateTime end);
    
    // Add this method to find waiting list records for a specific doctor and patient.
    List<WaitingAppointment> findByDoctorAndPatient(User doctor, User patient);
}
