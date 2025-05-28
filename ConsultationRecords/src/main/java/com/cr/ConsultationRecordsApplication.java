package com.cr; // Adjust your package as needed

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// Scan for entities from both ConsultationRecords and AppointmentScheduling modules.
@EntityScan(basePackages = { "com.cr.entity", "com.appschl.entity", "com.example.demo.entity" })
// Optionally, also scan your repositories from both modules.
@EnableJpaRepositories(basePackages = { "com.cr.repository", "com.appschl.repository", "com.example.demo.repository" })
public class ConsultationRecordsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsultationRecordsApplication.class, args);
    }
}
