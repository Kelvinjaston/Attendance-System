package com.project.attendance.system.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRegistrationRequest {

    @NotBlank(message = "Course code is required")
    private String courseCode;

    @NotBlank(message = "Course title is required")
    private String courseTitle;

    @NotBlank(message = "Lecturer Staff ID is required to assign the course.")
    private String lecturerStaffId;
    @NotBlank(message = "Semester is required")
    private String semester;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message ="Venue is required" )
    private String venue;

    private Double latitudeCenter;
    private Double longitudeCenter;
    private Integer allowedRadiusM;

}
