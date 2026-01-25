package com.project.attendance.system.security;

import com.project.attendance.system.models.Role;
import com.project.attendance.system.models.User;
import com.project.attendance.system.repo.RoleRepository;
import com.project.attendance.system.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class DataInitializer {

    @Bean
    CommandLineRunner initRolesAndAdmin(RoleRepository roleRepository,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

            if (userRepository.findByEmail("admin@system.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setUsername("Admin");
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);

                System.out.println("DEFAULT ADMIN CREATED: email=admin@system.com password=admin123");
            }
        };
    }
}
