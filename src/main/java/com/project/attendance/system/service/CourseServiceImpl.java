package com.project.attendance.system.service;

import com.project.attendance.system.dto.CourseRegistrationRequest;
import com.project.attendance.system.exception.LecturerNotFoundException;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.models.Lecturer;
import com.project.attendance.system.repo.CourseRepository;
import com.project.attendance.system.repo.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

   @Override
   @Transactional
    public void registerNewCourse(CourseRegistrationRequest request){
       if (courseRepository.findByCourseCode(request.getCourseCode()).isPresent())throw new IllegalStateException("Course with code" + request.getCourseCode() + "already exist");

       Lecturer lecturer = lecturerRepository.findByStaffId(request.getLecturerStaffId()).orElseThrow(()-> new LecturerNotFoundException(request.getLecturerStaffId()));

       Course course = new Course();
       course.setCourseCode(request.getCourseCode());
       course.setCourseTitle(request.getCourseTitle());
       course.setLatitudeCenter(request.getLatitudeCenter());
       course.setLongitudeCenter(request.getLongitudeCenter());
       course.setAllowedRadiusM(request.getAllowedRadiusM());
       course.setLecturer(lecturer);
       courseRepository.save(course);
   }
}