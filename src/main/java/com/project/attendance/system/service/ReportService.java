package com.project.attendance.system.service;

import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.repo.ReportRepository;
import com.project.attendance.system.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Attendance> findAllValidAttendanceRecords() {
        return reportRepository.findByIsValidTrueOrderByCheckInTimeDesc();
    }
    public Student findStudentByMatricNo(String matricNumber) throws Exception {
        return studentRepository.findByMatricNumber(matricNumber)
                .orElseThrow(() -> new Exception("Student profile not found for matric number: " + matricNumber));
    }
    public List<Attendance> findStudentAttendanceHistory(String matricNumber) {
        return reportRepository.findByStudent_MatricNumberAndIsValidTrue(matricNumber);
    }
}
