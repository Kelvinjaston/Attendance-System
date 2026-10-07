package com.project.attendance.system.controller;

import com.project.attendance.system.dto.CourseReportSummaryResponse;
import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.service.RegistrationService;
import com.project.attendance.system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private RegistrationService registrationService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/courses/summary")
    public ResponseEntity<List<CourseReportSummaryResponse>> getComprehensiveCourseReports() {
        try {
            List<CourseReportSummaryResponse> summary = reportService.compileComprehensiveCourseReports();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT') or hasAuthority('LECTURER')")    @GetMapping("/attendance/all")
    public ResponseEntity<List<Attendance>> getAllAttendanceRecords() {
        List<Attendance> records = reportService.findAllValidAttendanceRecords();
        return ResponseEntity.ok(records);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT') or hasAuthority('LECTURER')")
    @GetMapping("/student/profile")
    public ResponseEntity<Student> getStudentProfile(@RequestParam("matricNo") String matricNo) {
        try {
            Student student = reportService.findStudentByMatricNo(matricNo);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(registrationService.getAllRegisteredUsers());
    }

}