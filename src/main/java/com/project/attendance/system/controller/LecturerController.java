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
        try {
            String username = authentication.getName();

            List<CourseRegistrationRequest> courses = lecturerService.findCoursesByUsername(username);

            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            System.err.println(" Error fetching courses for lecturer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('LECTURER')")
    @GetMapping("/courses/{courseCode}")
    public ResponseEntity<?> getCourseDetails(
            Authentication authentication,
            @PathVariable String courseCode) {

        try {
            String username = authentication.getName();

            Optional<Course> courseDetails = lecturerService.findCourseDetailsByUsernameAndCourseCode(username, courseCode);

            if (courseDetails.isPresent()) {
                return ResponseEntity.ok(courseDetails.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course " + courseCode + " was not found or is not assigned to your account.");
            }

        } catch (Exception e) {
            System.err.println(" Error fetching details for " + courseCode + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving course details.");
        }
    }
}