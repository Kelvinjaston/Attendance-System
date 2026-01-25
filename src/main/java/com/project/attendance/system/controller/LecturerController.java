package com.project.attendance.system.controller;

import com.project.attendance.system.service.LecturerService;
import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer")
@CrossOrigin(origins = "http://localhost:3000")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @PreAuthorize("hasAuthority('LECTURER')")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseRegistrationRequest>> getAssignedCourses(Authentication authentication) {

        String staffId = authentication.getName();

        try {
            List<CourseRegistrationRequest> courses = lecturerService.findCoursesByStaffId(staffId);

            return ResponseEntity.ok(courses);

        } catch (Exception e) {
            System.err.println("Error fetching courses for staffId " + staffId + ": " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
    @PreAuthorize("hasAuthority('LECTURER')")
    @GetMapping("/courses/{courseCode}")
    public ResponseEntity<?> getCourseDetails(
            Authentication authentication,
            @PathVariable String courseCode) { // Captures the course code from the URL

        String staffId = authentication.getName();

        try {
            Optional<Course> courseDetails = lecturerService.findCourseDetailsByStaffIdAndCourseCode(staffId, courseCode);

            if (courseDetails.isPresent()) {
                return ResponseEntity.ok(courseDetails.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course " + courseCode + " not found or not assigned to this lecturer.");
            }

        } catch (Exception e) {
            System.err.println("Error fetching details for course " + courseCode + ": " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving course details.");
        }
    }
}