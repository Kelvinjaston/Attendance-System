package com.project.attendance.system.repo;

import com.project.attendance.system.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMatricNumberIgnoreCase(String matricNumber);
}