package com.project.attendance.system.controller;

import com.project.attendance.system.dto.AttendanceRequest;
import com.project.attendance.system.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/checking")
    public ResponseEntity<?> processAttendance(@Valid @RequestBody AttendanceRequest request) {
        try {
            String result = attendanceService.processCheckIn(request);

            if (result.startsWith("SUCCESS"))
                return ResponseEntity.ok(result);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing attendance: " + e.getMessage());
        }
    }
}
