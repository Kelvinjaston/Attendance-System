package com.project.attendance.system.repo;

import com.project.attendance.system.models.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer,Long> {
    Optional<Lecturer> findByStaffId(String staffId);

    Optional<Lecturer> findByUser_UserId(Long userId);
    Optional<Lecturer> findByUser_Username(String username);
}
