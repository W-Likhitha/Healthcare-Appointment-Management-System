package com.example.notification.repository;

import com.example.notification.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	@Query("SELECT a FROM Appointment a WHERE FORMATDATETIME(a.date, 'yyyy-MM-dd') = FORMATDATETIME(:date, 'yyyy-MM-dd')")
    List<Appointment> findByDate(@Param("date") Date date);
}
