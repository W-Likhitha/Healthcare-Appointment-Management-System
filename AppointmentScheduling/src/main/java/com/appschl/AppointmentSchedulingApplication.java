package com.appschl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//Scans for entity classes in both m2's and m1's packages:
@EntityScan(basePackages = {"com.appschl.entity", "com.example.demo.entity"})
//Scans for JPA repositories in both m2's and m1's packages:
@EnableJpaRepositories(basePackages = {"com.appschl.repository", "com.example.demo.repository"})
public class AppointmentSchedulingApplication {
  public static void main(String[] args) {
      SpringApplication.run(AppointmentSchedulingApplication.class, args);
  }
}

