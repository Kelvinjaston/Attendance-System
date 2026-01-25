package com.project.attendance.system.dto;

import lombok.Data;

@Data
public class LecturerRegistrationRequest {
    private String staffId;
    private String firstName;
    private String lastName;
    private String department;
    private String email;
    private String password;
}
