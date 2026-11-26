package com.harsha.paymentmanagement.service;

import com.harsha.paymentmanagement.dto.RegisterRequest;
import com.harsha.paymentmanagement.entity.AppUser;
import com.harsha.paymentmanagement.entity.Role;
import com.harsha.paymentmanagement.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new IllegalStateException("Username is already registered");
        }
        users.save(new AppUser(request.username(), encoder.encode(request.password()), Role.USER));
    }
}
