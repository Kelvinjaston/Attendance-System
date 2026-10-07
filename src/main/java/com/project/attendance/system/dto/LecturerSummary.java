package com.project.attendance.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerSummary {
    private String firstName;
    private String lastName;
    private String staffId;
    private String email;
    private String department;


}
