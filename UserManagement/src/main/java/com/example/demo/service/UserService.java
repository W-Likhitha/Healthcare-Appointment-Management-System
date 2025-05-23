package com.example.demo.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.*;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateDto;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    // Registers a new user and creates additional details based on role.
    @Transactional("transactionManager")
    public User registerUser(UserRequest request) {
        // Check for existing user by email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered. Please login");
        }
        // Proceed with registration
        User user = new User();
        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(request.getPassword());  // Already hashed

        User savedUser = userRepository.save(user);

        // Create role-specific details
        if (request.getRole() == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser);
            doctor.setSpecialization(request.getSpecialization());
            doctor.setQualification(request.getQualification());
            doctor.setRoomNumber(request.getRoomNumber());
            doctorRepository.save(doctor);
        } else if (request.getRole() == Role.PATIENT) {
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setDisease(request.getDisease());
            patient.setPlace(request.getPlace());
            patientRepository.save(patient);
        }

        return savedUser;
    }


    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    // Updates the user profile including any role-specific fields.
    @Transactional("transactionManager")
    public User updateUserProfile(Long userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update basic user information only if the DTO field is not null
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        // Persist changes to the common user fields.
        User savedUser = userRepository.save(user);
        
        // Role-specific update for DOCTOR
        if (user.getRole() == Role.DOCTOR) {
            Optional<Doctor> opDoc = doctorRepository.findByUser(user);
            if (opDoc.isPresent()) {
                Doctor doctor = opDoc.get();
                if (dto.getSpecialization() != null) {
                    doctor.setSpecialization(dto.getSpecialization());
                }
                if (dto.getQualification() != null) {
                    doctor.setQualification(dto.getQualification());
                }
                if (dto.getRoomNumber() != null) {
                    doctor.setRoomNumber(dto.getRoomNumber());
                }
                doctorRepository.save(doctor);
            }
        } 
        // Role-specific update for PATIENT
        else if (user.getRole() == Role.PATIENT) {
            Optional<Patient> opPat = patientRepository.findByUser(user);
            if (opPat.isPresent()) {
                Patient patient = opPat.get();
                if (dto.getDisease() != null) {
                    patient.setDisease(dto.getDisease());
                }
                if (dto.getPlace() != null) {
                    patient.setPlace(dto.getPlace());
                }
                patientRepository.save(patient);
            }
        }
        
        return savedUser;
    }

    

	public User createUser(User user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
		return null;
	}
}
