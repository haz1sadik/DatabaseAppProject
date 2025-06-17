package com.water.config;

import com.water.model.User;
import com.water.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdminUser(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123")); // default password
                admin.setRole("ROLE_ADMIN");
                admin.setAge(30);
                admin.setEmail("admin@example.com");
                admin.setPhone("0123456789");
                admin.setHeight(175.0);

                userRepo.save(admin);
                System.out.println("✅ Admin account created: admin / admin123");
            }
        };
    }
}
