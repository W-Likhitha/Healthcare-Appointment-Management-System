package com.example.notification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class HospitalNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalNotificationApplication.class, args);
    }
}
