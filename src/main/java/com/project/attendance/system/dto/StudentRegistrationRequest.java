package com.project.attendance.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRegistrationRequest {

    @NotBlank(message = "Matric number is required.")
    private String matricNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters.")
    private String password;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Department is required.")
    private String department;

    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Fingerprint template is required (Base64 string).")
    private String fingerprintTemplateBase64;
}
