package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        Optional<User> userOptional =
                userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOptional.get();

        model.addAttribute("user", user);

        return "user/dashboard";
    }
}