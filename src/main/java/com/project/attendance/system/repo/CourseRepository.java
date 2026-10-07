package com.project.attendance.system.repo;

import com.project.attendance.system.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByLecturer_StaffId(String staffId);

    Optional<Course> findByLecturer_StaffIdAndCourseCode(String staffId, String courseCode);

    List<Course> findByStudents_MatricNumber(String matricNumber);
}
