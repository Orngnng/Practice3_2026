package com.example.students.controller;

import com.example.students.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    public static final String SESSION_LOGGED_IN = "loggedIn";
    public static final String SESSION_USERNAME = "username";

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (userService.authenticate(username, password)) {
            session.setAttribute(SESSION_LOGGED_IN, true);
            session.setAttribute(SESSION_USERNAME, username);
            return "redirect:/students";
        }
        model.addAttribute("error", "Неверный логин или пароль");
        return "login-form";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register-form";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Логин и пароль не должны быть пустыми");
            return "register-form";
        }
        if (userService.usernameExists(username.trim())) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register-form";
        }
        userService.register(username.trim(), password);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
