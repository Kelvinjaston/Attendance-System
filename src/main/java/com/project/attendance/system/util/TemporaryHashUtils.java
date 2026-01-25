package com.project.attendance.system.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TemporaryHashUtils {
    public static void main(String[] args) {
        String clearPassword = "YourNewAdminPassword!";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String validHash = encoder.encode(clearPassword);

        System.out.println("--------------------------------------------------");
        System.out.println("Use this CLEAR password in Postman: " + clearPassword);
        System.out.println("Generated HASH to insert into MySQL:");
        System.out.println(validHash);
        System.out.println("--------------------------------------------------");
    }
}
