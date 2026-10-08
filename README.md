# Student Attendance System

A web-based student attendance management system that combines **fingerprint authentication** and **GPS/geolocation verification** to improve the reliability and integrity of student attendance records.

The system allows students to mark attendance only after the required authentication and location checks have been completed. It also provides role-based access for students, lecturers, and administrators.

## Overview

Traditional attendance methods such as paper registers, roll calls, and signatures can be time-consuming and may allow proxy attendance.

This project provides an automated attendance workflow that combines:

- User authentication
- WebAuthn-based fingerprint authentication
- Browser geolocation
- GPS geofencing
- Haversine distance calculation
- JWT authentication
- Role-Based Access Control (RBAC)
- Course and user management
- Attendance records and reports

The system follows a three-layer architecture consisting of a **ReactJS frontend**, **Spring Boot backend**, and **MySQL database**.

## Features

### Student

- Student registration and login
- Fingerprint authentication through WebAuthn
- Course-related attendance
- Browser location verification
- GPS/geofence validation
- Attendance history

### Lecturer

- Lecturer authentication
- Course management
- Monitor attendance for assigned courses
- View attendance records
- Generate/view attendance reports

### Administrator

- User management
- Student and lecturer registration
- Course management
- Location configuration
- Attendance monitoring
- Access to reports

## Attendance Verification

The attendance process follows these steps:

1. Student logs into the system.
2. The backend authenticates the student and issues a JWT.
3. Student selects the relevant course.
4. The system requests WebAuthn authentication.
5. The supported device performs fingerprint/user verification.
6. The browser requests the student's geographical location.
7. The frontend sends the attendance information to the backend.
8. Spring Security validates the JWT and user role.
9. The backend validates the course and authentication information.
10. The backend calculates the distance between the student's location and the configured course location using the Haversine formula.
11. The configured geofence radius is checked.
12. If all required checks pass, the attendance record is saved in MySQL.
13. If a required check fails, the attendance request is rejected.

### Geofencing

The system uses the **Haversine formula** to calculate the approximate distance between:

- The student's current latitude and longitude
- The configured course location

Attendance is accepted when:

```text
Calculated Distance <= Allowed Radius
