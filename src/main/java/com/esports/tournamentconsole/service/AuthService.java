package com.esports.tournamentconsole.service;

import com.esports.tournamentconsole.model.User;
import com.esports.tournamentconsole.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User login(String username, String rawPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(rawPassword, user.getPasswordHash()))
            throw new RuntimeException("Invalid password");

        return user;
    }
}