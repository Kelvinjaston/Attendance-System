package com.project.attendance.system.dto;

import lombok.Data;

@Data
public class EnrollmentRequest {

    private Long courseId;
    private String semester;
    private String level;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
