package com.example.user_management.config;

import com.example.user_management.entity.User;
import com.example.user_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Value("${app.superadmin.name:Super Admin}")
    private String adminName;

    @Value("${app.superadmin.email:admin@example.com}")
    private String adminEmail;

    @Value("${app.superadmin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                System.out.println("Superadmin already exists.");
                return;
            }

            User admin = new User();

            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(
                    passwordEncoder.encode(adminPassword)
            );
            admin.setRole("ROLE_SUPERADMIN");
            admin.setEnabled(true);

            userRepository.save(admin);

            System.out.println("Superadmin created successfully!");
        };
    }
}