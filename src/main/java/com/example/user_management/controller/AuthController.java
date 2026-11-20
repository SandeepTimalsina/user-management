package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Display registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    // Process registration
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        name = name.trim();
        email = email.trim().toLowerCase();

        // Validate name
        if (name.isEmpty()) {
            model.addAttribute("error", "Name is required.");
            return "auth/register";
        }

        // Validate email
        if (userService.emailExists(email)) {
            model.addAttribute("error", "Email already exists.");
            return "auth/register";
        }

        // Validate password
        if (password.length() < 8) {
            model.addAttribute(
                    "error",
                    "Password must be at least 8 characters."
            );
            return "auth/register";
        }

        // Confirm password
        if (!password.equals(confirmPassword)) {
            model.addAttribute(
                    "error",
                    "Passwords do not match."
            );
            return "auth/register";
        }

        // Create user
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Never accept roles from the registration form
        user.setRole("ROLE_USER");
        user.setEnabled(true);

        userService.saveUser(user);

        return "redirect:/login?registered=true";
    }

    // Display login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
}