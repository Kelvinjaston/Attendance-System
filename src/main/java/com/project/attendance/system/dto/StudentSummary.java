package com.project.attendance.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummary {
    private String firstName;
    private String lastName;
    private String email;
    private String matricNumber;
    private String department;

}
