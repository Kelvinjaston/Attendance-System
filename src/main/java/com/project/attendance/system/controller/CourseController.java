package com.project.attendance.system.controller;

import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.exception.LecturerNotFoundException;
import com.project.attendance.system.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> registerCourse(@Valid @RequestBody CourseRegistrationRequest request) {

        try {
            courseService.registerNewCourse(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    "SUCCESS: Course '" + request.getCourseTitle() +
                            "' (" + request.getCourseCode() + ") registered successfully and assigned to Lecturer: " + request.getLecturerStaffId()
            );

        } catch (LecturerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Course registration failed due to an internal error: " + e.getMessage());
        }
    }
}