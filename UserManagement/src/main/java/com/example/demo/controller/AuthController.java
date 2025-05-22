package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

 @Autowired
 private UserService userService;
 
 @Autowired
 private UserRepository userRepository;

@Autowired
 private JwtUtil jwtUtil;
 
 @Autowired
 private BCryptPasswordEncoder passwordEncoder;

 // Registration endpoint
 @PostMapping("/register")
 public ResponseEntity<?> registerUser(@RequestBody UserRequest request) {
     // Hash plaintext password before storing it
     request.setPassword(passwordEncoder.encode(request.getPassword()));
     User savedUser = userService.registerUser(request);
     return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
 }

 // Login endpoint
 @PostMapping("/login")
 public ResponseEntity<?> login(@RequestBody AuthRequest request) {
     Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
     if (userOpt.isPresent()) {
         User user = userOpt.get();
         if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
             String token = jwtUtil.generateToken(user);
             return ResponseEntity.ok().body("{ \"token\": \"" + token + "\" }");
         }
     }
     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
 }

}

