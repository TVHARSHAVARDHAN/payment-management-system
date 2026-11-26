
package com.harsha.paymentmanagement.config;

import com.harsha.paymentmanagement.entity.AppUser;
import com.harsha.paymentmanagement.entity.Role;
import com.harsha.paymentmanagement.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapAdmin {

    @Bean
    CommandLineRunner createInitialAdmin(
            AppUserRepository users,
            PasswordEncoder encoder,
            @Value("${app.admin.username:admin}") String username,
            @Value("${app.admin.password}") String password) {

        return args -> {
            users.findByUsername(username).ifPresentOrElse(
                admin -> {
                    admin.setPassword(encoder.encode(password));
                    admin.setRole(Role.ADMIN);
                    users.save(admin);
                    System.out.println("Admin credentials synchronized.");
                },
                () -> {
                    users.save(new AppUser(
                        username,
                        encoder.encode(password),
                        Role.ADMIN
                    ));
                    System.out.println("Initial admin created.");
                }
            );
        };
    }
}
