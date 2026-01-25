package com.project.attendance.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRequest {

    @NotBlank(message = "Matric number is required.")
    private String matricNumber;

    @NotBlank(message = "Course code is required.")
    private String courseCode;

    @NotBlank(message = "Fingerprint hash is required.")
    private String fingerprintHash;

    @NotNull(message = "Latitude is required.")
    private Double latitude;

    @NotNull(message = "Longitude is required.")
    private Double longitude;
}
