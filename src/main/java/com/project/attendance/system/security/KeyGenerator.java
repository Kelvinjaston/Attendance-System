package com.project.attendance.system.security;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64];
        secureRandom.nextBytes(keyBytes);

        String secretKey = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("------------------------------------------------------------------");
        System.out.println("Generated Secure 512-bit JWT Secret Key:");
        System.out.println(secretKey);
        System.out.println("------------------------------------------------------------------");
        System.out.println("USE THIS KEY TO SET THE JWT_SECRET_KEY ENVIRONMENT VARIABLE.");
    }
}
