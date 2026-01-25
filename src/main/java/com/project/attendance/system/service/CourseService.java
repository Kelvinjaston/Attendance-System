package com.project.attendance.system.service;

import com.project.attendance.system.dto.CourseRegistrationRequest;

public interface CourseService {
    void registerNewCourse(CourseRegistrationRequest request);
}
