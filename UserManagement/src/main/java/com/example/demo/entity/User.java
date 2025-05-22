package com.example.demo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Entity
//@Table(name = "users")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

//@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String gender;
    private String phone;
    private String email;
    
    @JsonIgnore
    private String password;  // Already hashed

    @Enumerated(EnumType.STRING)
    private Role role;  // DOCTOR or PATIENT

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Doctor doctorDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Patient patientDetails;
}
