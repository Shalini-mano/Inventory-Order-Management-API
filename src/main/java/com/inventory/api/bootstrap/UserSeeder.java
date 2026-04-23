package com.inventory.api.bootstrap;
import com.inventory.api.model.Role;
import com.inventory.api.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.inventory.api.repo.UserRepository;

import java.time.LocalDateTime;

/**
 * What's the purpose? create 3 User objects
 * Dependencies? UserRepository, PasswordEncoder
 *  - id, name, email, passwordHash, roles...
 *  Set up: @Component, implement CLR
 */
@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // The order must match your User entity constructor exactly:
        // Example: (id, username, email, passwordHash, role)

        User admin = new User(
                "admin",                            // username
                "admin@example.com",                // email
                passwordEncoder.encode("admin123"), // passwordHash
                Role.ADMIN                          // role
        );

        User user = new User(
                "user",
                "user@example.com",
                passwordEncoder.encode("user123"),
                Role.USER
        );

        userRepository.save(admin);
        userRepository.save(user);

        System.out.println("Seeded database with Admin and User accounts.");
    }
}
