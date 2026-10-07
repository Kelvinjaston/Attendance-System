package com.project.attendance.system.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "students"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(unique = true, nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String courseTitle;

    @Column(nullable = false)
    private String venue;

    private Double latitudeCenter;

    private Double longitudeCenter;

    @Column(name = "allowed_radius")
    private Integer allowedRadiusM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_staff_id", referencedColumnName = "staffId")
    private Lecturer lecturer;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> students = new HashSet<>();
}