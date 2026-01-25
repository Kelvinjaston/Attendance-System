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

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Arrays;

@Service
public class AttendanceService {
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private AttendanceRepository attendanceRepository;

    private static final int R = 6371000;

    public String processCheckIn(AttendanceRequest request) throws Exception {

        Student student = studentRepository.findByMatricNumber(request.getMatricNumber())
                .orElseThrow(() -> new Exception("Error: Student record not found for authenticated user."));

        boolean fingerprintPassed = checkFingerprint(
                student.getFingerprintTemplate(),
                request.getFingerprintHash()
        );

        if (!fingerprintPassed) {
            return "FAILURE: Biometric authentication failed.";
        }

        Course course = courseRepository.findByCourseCode(request.getCourseCode())
                .orElseThrow(() -> new Exception("Error: Course location is not registered in the system."));

        double distanceMeters = calculateDistanceMeters(
                course.getLatitudeCenter(),
                course.getLongitudeCenter(),
                request.getLatitude(),
                request.getLongitude()
        );

        boolean isWithinBounds = distanceMeters <= course.getAllowedRadiusM();

        if (!isWithinBounds) {
            return String.format("FAILURE: Location check failed. Distance: %.2f meters away from boundary.", distanceMeters);
        }

        Attendance record = new Attendance();
        record.setStudent(student);
        record.setCourse(course);
        record.setCheckInLatitude(request.getLatitude());
        record.setCheckInLongitude(request.getLongitude());
        record.setIsValid(true);

        attendanceRepository.save(record);

        return "SUCCESS: Attendance recorded instantly. Validation Passed.";
    }
    private double calculateDistanceMeters(double centerLat, double centerLon, double studentLat, double studentLon) {
        double dLat = Math.toRadians(studentLat - centerLat);
        double dLon = Math.toRadians(studentLon - centerLon);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(centerLat)) *
                                Math.cos(Math.toRadians(studentLat)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private boolean checkFingerprint(byte[] storedHash, String receivedTemplateBase64) {
        try {
            byte[] receivedTemplateBytes = Base64.getDecoder().decode(receivedTemplateBase64);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] receivedTemplateHash = md.digest(receivedTemplateBytes);

            return Arrays.equals(storedHash, receivedTemplateHash);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
