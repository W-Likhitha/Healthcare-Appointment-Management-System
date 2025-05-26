package com.appschl.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appschl.entity.Appointment;
import com.example.demo.entity.User;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Retrieve appointments for a given patient (by user id)
    List<Appointment> findByPatient_UserId(Long patientId);
    
    // Retrieve appointments for a given doctor (by user id)
    List<Appointment> findByDoctor_UserId(Long doctorId);
    
    // Retrieve all appointments for the specified doctor on the specified date.
    List<Appointment> findByDoctorAndAppointmentDate(User doctor, LocalDate appointmentDate);

	Appointment findFirstByDoctorAndPatientAndAppointmentDateAndAppointmentTime(User doctor, User patient,
			LocalDate date, LocalTime startTime);
}
