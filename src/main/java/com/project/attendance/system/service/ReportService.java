package com.project.attendance.system.service;

import com.project.attendance.system.dto.*;
import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.models.User;
import com.project.attendance.system.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> findAllValidAttendanceRecords() {
        return reportRepository.findByIsValidTrueOrderByCreatedAtDesc();
    }
    public Student findStudentByMatricNo(String matricNumber) throws Exception {
        return studentRepository.findByMatricNumberIgnoreCase(matricNumber)
                .orElseThrow(() -> new Exception("Student profile not found for matric number: " + matricNumber));
    }
    public List<Attendance> findStudentAttendanceHistory(String matricNumber) {
        return reportRepository.findByStudent_MatricNumberAndIsValidTrue(matricNumber);
    }
    public List<User> getAllRegisteredUsers() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<CourseReportSummaryResponse> compileComprehensiveCourseReports() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {

            LecturerSummary lecturerDto = null;
            if (course.getLecturer() != null) {
                lecturerDto = new LecturerSummary(
                        course.getLecturer().getFirstName(),
                        course.getLecturer().getLastName(),
                        course.getLecturer().getStaffId(),
                        course.getLecturer().getEmail(),
                        course.getDepartment()
                );
            }

            List<StudentSummary> studentCohort = course.getStudents().stream()
                    .map(s -> new StudentSummary(
                            s.getFirstName(),
                            s.getLastName(),
                            s.getEmail(),
                            s.getMatricNumber(),
                            s.getDepartment()
                    )).collect(Collectors.toList());


            List<AttendanceLog> attendanceLogs = attendanceRepository.findByCourse_CourseCode(course.getCourseCode())
                    .stream()
                    .map(log -> new AttendanceLog(
                            log.getStudent().getFirstName(),
                            log.getStudent().getLastName(),
                            log.getStudent().getMatricNumber(),
                            log.getCreatedAt().toString(),
                            log.getIsValid() ? "ON TIME" : "INVALID"
                    )).collect(Collectors.toList());

            int enrolled = studentCohort.size();
            int present = attendanceLogs.size();
            int absent = Math.max(0, enrolled - present);
            int rate = enrolled > 0 ? (present * 100) / enrolled : 0;

            ReportMetrics metricsDto = new ReportMetrics(enrolled, present, absent, rate);

            return new CourseReportSummaryResponse(
                    course.getCourseCode(),
                    course.getCourseTitle(),
                    course.getDepartment(),
                    course.getSemester(),
                    course.getLatitudeCenter(),
                    course.getLongitudeCenter(),
                    course.getAllowedRadiusM(),
                    lecturerDto,
                    studentCohort,
                    attendanceLogs,
                    metricsDto
            );
        }).collect(Collectors.toList());
    }
}
