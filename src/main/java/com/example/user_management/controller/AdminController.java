package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {

        // Only normal users are included in the management table.
        List<User> users = userService.getAllUsers()
                .stream()
                .filter(user -> "ROLE_USER".equals(user.getRole()))
                .toList();

        long totalUsers = users.size();

        long activeUsers = users.stream()
                .filter(User::isEnabled)
                .count();

        long inactiveUsers = totalUsers - activeUsers;

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("inactiveUsers", inactiveUsers);

        return "admin/dashboard";
    }
}