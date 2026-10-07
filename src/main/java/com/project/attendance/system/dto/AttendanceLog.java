package com.project.attendance.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLog {
    private String firstName;
    private String lastName;
    private String matricNumber;
    private String timestamp;
    private String status;

}
