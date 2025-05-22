package com.example.demo.dto;

import com.example.demo.entity.Role;
import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String gender;
    private String phone;
    private String email;
    private Role role;
    private String password;
    
    // Plaintext password (to be hashed before saving)

    // Doctor-specific fields
    private String specialization;
    private String qualification;
    private String roomNumber;

    // Patient-specific fields
    private String disease;
    private String place;
}