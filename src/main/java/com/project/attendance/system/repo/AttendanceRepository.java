package com.project.attendance.system.repo;

import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByCourse_CourseCode(String courseCode);
    List<Attendance> findByCourse_CourseCodeAndDateOrderByCreatedAtDesc(String courseCode, LocalDate date);

    boolean existsByStudentAndCourseAndDate(Student student, Course course, LocalDate now);


    List<Attendance> findByStudent_MatricNumberIgnoreCaseOrderByCreatedAtDesc(String matricNumber);

    List<Attendance> findByCourse(Course course);
}