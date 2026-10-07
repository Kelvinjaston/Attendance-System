package com.project.attendance.system.controller;

import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.dto.EnrollmentRequest;
import com.project.attendance.system.exception.LecturerNotFoundException;
import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.repo.AttendanceRepository;
import com.project.attendance.system.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceRepository attendanceRepository;

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

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'LECTURER')")
    public ResponseEntity<?> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<Map<String, Object>> response = courses.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", c.getCourseId());
            map.put("courseCode", c.getCourseCode());
            map.put("courseTitle", c.getCourseTitle());
            map.put("department", c.getDepartment());
            map.put("venue",c.getVenue());

            List<Map<String, Object>> enrolledStudents = c.getStudents().stream().map(s -> {
                Map<String, Object> sMap = new HashMap<>();
                sMap.put("matricNumber", s.getMatricNumber());
                sMap.put("firstName", s.getFirstName());
                sMap.put("lastName", s.getLastName());
                sMap.put("email", s.getEmail());
                sMap.put("department", s.getDepartment());
                return sMap;
            }).collect(Collectors.toList());
            map.put("enrolledStudents", enrolledStudents);

            if (c.getLecturer() != null) {
                Map<String, Object> lecMap = new HashMap<>();
                lecMap.put("staffId", c.getLecturer().getStaffId());
                lecMap.put("firstName", c.getLecturer().getFirstName());
                lecMap.put("lastName", c.getLecturer().getLastName());
                lecMap.put("email", c.getLecturer().getEmail());
                lecMap.put("department", c.getLecturer().getDepartment());
                map.put("lecturer", lecMap);
            }

            List<Attendance> attendanceRecords = attendanceRepository.findByCourse(c);
            List<Map<String, Object>> attendanceLogs = attendanceRecords.stream().map(a -> {
                Map<String, Object> aMap = new HashMap<>();
                aMap.put("matricNumber", a.getStudent().getMatricNumber());
                aMap.put("firstName", a.getStudent().getFirstName());
                aMap.put("lastName", a.getStudent().getLastName());
                aMap.put("timestamp", a.getCreatedAt() != null ? a.getCreatedAt().toString() : null);
                aMap.put("status", Boolean.TRUE.equals(a.getIsValid()) ? "PRESENT" : "INVALID");
                aMap.put("verified", a.getIsValid());
                return aMap;
            }).collect(Collectors.toList());
            map.put("attendanceLogs", attendanceLogs);

            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/enrolled")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> getMyEnrolledCourses(Principal principal) {
        try {
            String matricNumber = principal.getName();
            List<Course> enrolled = courseService.getCoursesByStudent(matricNumber);

            List<Map<String, Object>> response = enrolled.stream().map(c -> {
                Map<String, Object> map = new HashMap<>();
                map.put("courseId", c.getCourseId());
                map.put("courseCode", c.getCourseCode());
                map.put("courseTitle", c.getCourseTitle());
                map.put("latitudeCenter", c.getLatitudeCenter());
                map.put("longitudeCenter", c.getLongitudeCenter());
                map.put("allowedRadiusM", c.getAllowedRadiusM());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{courseId}/unenroll")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> unenrollFromCourse(@PathVariable Long courseId, Principal principal) {
        try {
            String matricNumber = principal.getName();

            courseService.unenrollStudent(courseId, matricNumber);

            return ResponseEntity.ok("Successfully dropped the course.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to drop course: " + e.getMessage());
        }

    }
    @PostMapping("/enroll")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> enrollInCourse(@RequestBody EnrollmentRequest request, Principal principal) {
        try {
            String matricNumber = principal.getName();

            courseService.enrollStudent(
                    request.getCourseId(),
                    matricNumber,
                    request.getSemester(),
                    request.getLevel()
            );

            return ResponseEntity.ok("Enrolled successfully in " + request.getSemester() + " semester!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Enrollment failed: " + e.getMessage());
        }
    }
}