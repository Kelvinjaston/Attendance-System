package com.project.attendance.system.service;

import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Lecturer;
import com.project.attendance.system.repo.AttendanceRepository;
import com.project.attendance.system.repo.CourseRepository;
import com.project.attendance.system.repo.LecturerRepository;
import com.project.attendance.system.dto.CourseRegistrationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private Lecturer getLecturerByUsername(String username) {
        return lecturerRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Lecturer not found for user: " + username));
    }
    public List<CourseRegistrationRequest> findCoursesByUsername(String username) {

        Lecturer lecturer = getLecturerByUsername(username);

        List<Course> courses = courseRepository.findByLecturer_StaffId(lecturer.getStaffId());

        return courses.stream()
                .map(course -> new CourseRegistrationRequest(
                        course.getCourseCode(),
                        course.getCourseTitle(),
                        lecturer.getStaffId(),
                        course.getSemester(),
                        course.getDepartment(),
                        course.getVenue(),
                        course.getLatitudeCenter(),
                        course.getLongitudeCenter(),
                        course.getAllowedRadiusM()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Course> findCourseDetailsByUsernameAndCourseCode(String username, String courseCode) {

        Lecturer lecturer = getLecturerByUsername(username);

        return courseRepository.findByLecturer_StaffIdAndCourseCode(
                lecturer.getStaffId(),
                courseCode
        );
    }

    public List<Attendance> getAttendanceRecordsForCourse(String username, String courseCode) {

        Lecturer lecturer = getLecturerByUsername(username);

        return courseRepository
                .findByLecturer_StaffIdAndCourseCode(lecturer.getStaffId(), courseCode)
                .map(course -> attendanceRepository.findByCourse_CourseCode(courseCode))
                .orElse(List.of());
    }
    public boolean markManualAttendance(String username, String courseCode) {
        return true;
    }
}