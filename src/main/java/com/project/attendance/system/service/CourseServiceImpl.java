package com.project.attendance.system.service;

import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.exception.LecturerNotFoundException;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Lecturer;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.repo.CourseRepository;
import com.project.attendance.system.repo.LecturerRepository;
import com.project.attendance.system.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    @Transactional
    public void registerNewCourse(CourseRegistrationRequest request) {
        if (courseRepository.findByCourseCode(request.getCourseCode()).isPresent()) {
            throw new IllegalStateException("Course with code " + request.getCourseCode() + " already exists");
        }

        Lecturer lecturer = lecturerRepository.findByStaffId(request.getLecturerStaffId())
                .orElseThrow(() -> new LecturerNotFoundException(request.getLecturerStaffId()));

        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseTitle(request.getCourseTitle());
        course.setLatitudeCenter(request.getLatitudeCenter());
        course.setLongitudeCenter(request.getLongitudeCenter());
        course.setAllowedRadiusM(request.getAllowedRadiusM());
        course.setSemester(request.getSemester());
        course.setDepartment(request.getDepartment());
        course.setVenue(request.getVenue());
        course.setLecturer(lecturer);

        courseRepository.save(course);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public void enrollStudent(Long courseId, String matricNumber, String semester, String level) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        Student student = studentRepository.findByMatricNumberIgnoreCase(matricNumber.trim())
                .orElseThrow(() -> new RuntimeException("Student record not found: " + matricNumber));

        if (course.getDepartment() != null && student.getDepartment() != null) {
            if (!course.getDepartment().equalsIgnoreCase(student.getDepartment())) {
                throw new RuntimeException(
                        "Access Denied: Restricted to the " + course.getDepartment() + " department."
                );
            }
        }

        if (student.getCourses().contains(course)) {
            throw new IllegalStateException("You are already enrolled in this course.");
        }

        student.getCourses().add(course);

        course.getStudents().add(student);

        studentRepository.save(student);
    }
    @Override
    public List<Course> getCoursesByStudent(String matricNumber) {
        return courseRepository.findByStudents_MatricNumber(matricNumber);
    }

    @Override
    @Transactional
    public void unenrollStudent(Long courseId, String matricNumber) {

        Student student = studentRepository.findByMatricNumberIgnoreCase(matricNumber.trim())
                .orElseThrow(() -> new RuntimeException("Student not found: " + matricNumber));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        student.getCourses().remove(course);

        course.getStudents().remove(student);

        studentRepository.save(student);
    }
}