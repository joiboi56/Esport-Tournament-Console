package com.esports.tournamentconsole.controller;

import com.esports.tournamentconsole.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("role", user.getRole().name());
        return "home";


    }
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
}