package com.example.notification.util;

import com.example.notification.model.User;
import com.example.notification.repository.UserRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import jakarta.annotation.*;

@Component
public class DataInitalizer {

	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	public void init() {
		for(int i =1; i<=50; i++) {
			User user = new User();
			user.setName("User"+i);
			user.setEmail("user"+i+"@gmail.com");
			user.setRole(i<=10?"DOCTOR":"PATIENT");
			userRepository.save(user);
		}
	}
	
}
