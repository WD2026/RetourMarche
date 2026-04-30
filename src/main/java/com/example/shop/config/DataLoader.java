package com.example.shop.config;

import com.example.shop.User.User;
import com.example.shop.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail("admin@gmail.com");
            
            String initialAdminPassword = System.getenv("ADMIN_PASSWORD");
            if (initialAdminPassword == null || initialAdminPassword.isBlank()) {
                log.error("ADMIN_PASSWORD environment variable is not set. Skipping admin user creation.");
                return;
            }
            admin.setPassword(passwordEncoder.encode(initialAdminPassword));
            admin.setTelephone("0123456789"); // Valid French format
            admin.setRole("ADMIN");
            userRepository.save(admin);
            log.info("Admin user created successfully.");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
