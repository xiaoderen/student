package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.entity.User;
import com.example.attendancesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String listStudents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "admin/student-list";
    }

    @GetMapping("/students/add")
    public String addStudentPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "admin/student-form";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute Student student, HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            if (student.getId() == null) {
                studentService.createStudent(student);
            } else {
                studentService.updateStudent(student);
            }
            return "redirect:/admin/students";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", student);
            model.addAttribute("isEdit", student.getId() != null);
            return "admin/student-form";
        }
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentPage(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/admin/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("isEdit", true);
        return "admin/student-form";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
}
