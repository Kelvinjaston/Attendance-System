package com.project.attendance.system.service;

import com.project.attendance.system.dto.AttendanceRequest;
import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.repo.AttendanceRepository;
import com.project.attendance.system.repo.CourseRepository;
import com.project.attendance.system.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@Service
public class AttendanceService {

    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private AttendanceRepository attendanceRepository;

    private static final int R = 6371000;

    public String processCheckIn(AttendanceRequest request) throws Exception {
        if (request.getMatricNumber() == null) {
            throw new Exception("Error: Matric Number is missing from request.");
        }

        String searchMatric = request.getMatricNumber().trim();

        Student student = studentRepository.findByMatricNumberIgnoreCase(searchMatric)
                .orElseThrow(() -> new Exception("Error: Student record not found for: " + searchMatric));

        boolean fingerprintPassed = checkFingerprint(
                student.getFingerprintTemplate(),
                request.getFingerprintHash()
        );
        if (!fingerprintPassed) {
            return "FAILURE: Biometric authentication failed.";
        }

        Course course = courseRepository.findByCourseCode(request.getCourseCode())
                .orElseThrow(() -> new Exception("Error: Course session not found."));

        boolean alreadyMarked = attendanceRepository.existsByStudentAndCourseAndDate(
                student,
                course,
                LocalDate.now()
        );
        if (alreadyMarked) {
            return "FAILURE: Attendance already recorded for this session today.";
        }
        double distanceMeters = calculateDistanceMeters(
                course.getLatitudeCenter(),
                course.getLongitudeCenter(),
                request.getLatitude(),
                request.getLongitude()
        );
        if (distanceMeters > course.getAllowedRadiusM()) {
            return String.format("FAILURE: Location check failed. You are %.2f meters away.", distanceMeters);
        }
        Attendance record = new Attendance();
        record.setStudent(student);
        record.setCourse(course);
        record.setCheckInLatitude(request.getLatitude());
        record.setCheckInLongitude(request.getLongitude());
        record.setIsValid(true);
        record.setCreatedAt(LocalDateTime.now());
        record.setDate(LocalDate.now());

        attendanceRepository.save(record);
        return "SUCCESS: Attendance recorded instantly. Validation Passed.";
    }
    public boolean checkFingerprint(String storedHash, String receivedHash) {
        if (storedHash == null || receivedHash == null) {
            System.out.println("Biometric Error: Stored or Received hash is NULL");
            return false;
        }

        boolean match = storedHash.equals(receivedHash);
        if (!match) {
            System.out.println("Biometric Mismatch!");
            System.out.println("Stored: " + storedHash.substring(0, 10) + "...");
            System.out.println("Received: " + receivedHash.substring(0, 10) + "...");
        }
        return match;
    }
    public List<Attendance> getLiveAttendanceRecords(String courseCode) {
        return attendanceRepository.findByCourse_CourseCodeAndDateOrderByCreatedAtDesc(courseCode, LocalDate.now());
    }
    private double calculateDistanceMeters(double centerLat, double centerLon, double studentLat, double studentLon) {
        double dLat = Math.toRadians(studentLat - centerLat);
        double dLon = Math.toRadians(studentLon - centerLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(centerLat)) *
                        Math.cos(Math.toRadians(studentLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}