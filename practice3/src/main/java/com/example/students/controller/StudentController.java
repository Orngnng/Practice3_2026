package com.example.students.controller;

import com.example.students.model.Student;
import com.example.students.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute(AuthController.SESSION_LOGGED_IN));
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("username", session.getAttribute(AuthController.SESSION_USERNAME));
        return "students-list";
    }

    @GetMapping("/add")
    public String showForm(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("student", new Student());
        return "students-form";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("student") Student student, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }
}
