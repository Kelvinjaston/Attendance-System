package com.project.attendance.system.service;

import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.models.Course;
import java.util.List;

public interface CourseService {

    void registerNewCourse(CourseRegistrationRequest request);

    List<Course> getAllCourses();

    void enrollStudent(Long courseId, String matricNumber,String semester,String level);

    List<Course> getCoursesByStudent(String matricNumber);

    void unenrollStudent(Long courseId, String matricNumber);
}