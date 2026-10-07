package com.project.attendance.system.repo;

import com.project.attendance.system.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByIsValidTrueOrderByCreatedAtDesc();

    List<Attendance> findByStudent_MatricNumberAndIsValidTrue(String matricNumber);
}
