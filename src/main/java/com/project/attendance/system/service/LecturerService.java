package com.project.attendance.system.service;

import com.project.attendance.system.models.Attendance;
import com.project.attendance.system.models.Course;
import com.project.attendance.system.repo.AttendanceRepository;
import com.project.attendance.system.repo.CourseRepository;
import com.project.attendance.system.repo.LecturerRepository;
import com.project.attendance.system.dto.CourseRegistrationRequest; // Required for clean API response
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LecturerService {
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private  AttendanceRepository attendanceRepository;

    public List<CourseRegistrationRequest>findCoursesByStaffId(String staffId){
        List<Course> courses = courseRepository.findByLecturer_StaffId(staffId);
        return courses.stream()
                .map(course -> new CourseRegistrationRequest(
                        course.getCourseCode(),
                        course.getCourseTitle(),
                        course.getLecturer().getStaffId(),
                        course.getLatitudeCenter(),
                        course.getLongitudeCenter(),
                        course.getAllowedRadiusM()
                )).collect(Collectors.toList());
    }
    public Optional<Course>findCourseDetailsByStaffIdAndCourseCode(String staffId,String courseCode){
       return courseRepository.findByLecturer_StaffIdAndCourseCode(staffId,courseCode);
    }
    public List<Attendance>getAttendanceRecordsForCourse(String staffId,String courseCode){
        return courseRepository.findByLecturer_StaffIdAndCourseCode(staffId,courseCode)
                .map(course -> attendanceRepository.findByCourse_CourseCode(courseCode)).orElse(List.of());

    }
    public boolean markManualAttendance(String staffId,String courseCode){
        return true;
    }
}
