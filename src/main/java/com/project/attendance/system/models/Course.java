package com.project.attendance.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(unique = true, nullable = false)
    private String courseCode;

    private String courseTitle;

    private Double latitudeCenter;

    private Double longitudeCenter;

    @Column(name = "allowed_radius")
    private Integer allowedRadiusM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_staff_id", referencedColumnName = "staffId")
    private Lecturer lecturer;

}