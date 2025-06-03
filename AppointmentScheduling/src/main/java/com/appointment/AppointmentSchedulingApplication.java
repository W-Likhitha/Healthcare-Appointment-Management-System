package com.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
@EntityScan(basePackages = {"com.appointment.entity", "com.example.demo.entity"})
@EnableJpaRepositories(basePackages = {"com.appointment.repository", "com.example.demo.repository"})
public class AppointmentSchedulingApplication {
  public static void main(String[] args) {
      SpringApplication.run(AppointmentSchedulingApplication.class, args);
  }
}
