package com.example.demo.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String name;
    private String gender;
    private String phone;
    
    // Doctor fields (if applicable)
    private String specialization;
    private String qualification;
    private String roomNumber;
    
    // Patient fields (if applicable)
    private String disease;
    private String place;
}
