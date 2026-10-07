package com.project.attendance.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseReportSummaryResponse {
    private String courseCode;
    private String courseTitle;
    private String department;
    private String semester;
    private Double latitudeCenter;
    private Double longitudeCenter;
    private Integer allowedRadiusM;
    private LecturerSummary lecturer;
    private List<StudentSummary> enrolledStudents;
    private List<AttendanceLog> attendanceLogs;
    private ReportMetrics metrics;
}
