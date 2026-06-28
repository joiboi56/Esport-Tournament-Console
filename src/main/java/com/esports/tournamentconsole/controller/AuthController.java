package com.esports.tournamentconsole.controller;

import com.esports.tournamentconsole.model.Role;
import com.esports.tournamentconsole.model.User;
import com.esports.tournamentconsole.repository.UserRepository;
import com.esports.tournamentconsole.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session, Model model) {
        try {
            User user = authService.login(username, password);
            session.setAttribute("loggedUser", user);
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register-account")
    public String registerPage() {
        return "login";
    }

    @PostMapping("/register-account")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("registerError", "Passwords do not match");
            return "login";
        }

        if (userRepo.findByUsername(username).isPresent()) {
            model.addAttribute("registerError", "Username already taken");
            return "login";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(encoder.encode(password));
        newUser.setRole(Role.PLAYER);
        userRepo.save(newUser);

        model.addAttribute("registerSuccess", "Account created! You can now log in.");
        return "login";
    }
}