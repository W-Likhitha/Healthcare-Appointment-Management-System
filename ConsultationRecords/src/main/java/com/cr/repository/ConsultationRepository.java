package com.cr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cr.entity.Consultation;
import com.appschl.entity.Appointment;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    // Finds the consultation record associated with a given appointment.
    Consultation findByAppointment(Appointment appointment);
}
