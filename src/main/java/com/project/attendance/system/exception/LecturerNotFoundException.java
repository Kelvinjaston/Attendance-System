package com.project.attendance.system.exception;

public class LecturerNotFoundException extends RuntimeException {
    public LecturerNotFoundException(String staffId) {
        super("Lecturer with Staff ID: " + staffId + " not found.");
    }
}
