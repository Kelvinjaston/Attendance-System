package com.project.attendance.system.controller;
import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/attendance/all")
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
}