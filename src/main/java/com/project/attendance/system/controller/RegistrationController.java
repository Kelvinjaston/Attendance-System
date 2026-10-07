package com.project.attendance.system.controller;

import com.project.attendance.system.dto.AdminRegistrationRequest;
import com.project.attendance.system.dto.LecturerRegistrationRequest;
import com.project.attendance.system.dto.StudentRegistrationRequest;
import com.project.attendance.system.repo.UserRepository;
import com.project.attendance.system.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        try {
            registrationService.registerNewStudent(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("SUCCESS: Student registered successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/lecturer")
    public ResponseEntity<String> registerLecturer(@Valid @RequestBody LecturerRegistrationRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("--- 🛡️ SECURITY DEBUG ---");
        if (auth != null) {
            System.out.println("User: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
            System.out.println("Is Authenticated: " + auth.isAuthenticated());
        } else {
            System.out.println("Authentication object is NULL (User is Anonymous)");
        }
        System.out.println("-------------------------");

        try {
            registrationService.registerNewLecturer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    "SUCCESS: Lecturer registered successfully with Staff ID: " + request.getStaffId() + ". User account created."
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        try {
            registrationService.registerNewAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS: Admin account created.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Admin registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = registrationService.getAllRegisteredUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }
    }
}