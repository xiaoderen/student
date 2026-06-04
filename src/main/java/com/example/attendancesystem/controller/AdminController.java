package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.entity.User;
import com.example.attendancesystem.service.CourseSelectionService;
import com.example.attendancesystem.service.StudentService;
import com.example.attendancesystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseSelectionService courseSelectionService;

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
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
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
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
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
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
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
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
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
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }

    @PostMapping("/students/import")
    @ResponseBody
    public Result<Map<String, Object>> importStudents(@RequestParam("file") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return Result.error("权限不足，只有教师和管理员可以导入学生信息");
        }

        try {
            Map<String, Object> result = studentService.importStudentsFromExcel(file);
            if ((Boolean) result.get("success")) {
                return Result.success(result);
            } else {
                return Result.error((String) result.get("message"));
            }
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/students/gender-stats")
    @ResponseBody
    public Result<Map<String, Long>> getGenderStats(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return Result.error("权限不足");
        }

        try {
            Map<String, Long> stats = studentService.getGenderStatistics();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    @PostMapping("/course-selections/import")
    @ResponseBody
    public Result<Map<String, Object>> importCourseSelections(@RequestParam("file") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return Result.error("权限不足");
        }

        try {
            Map<String, Object> result = courseSelectionService.importCourseSelectionsFromExcel(file);
            if ((Boolean) result.get("success")) {
                return Result.success(result);
            } else {
                return Result.error((String) result.get("message"));
            }
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/course-selections")
    public String manageCourseSelections(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "admin/courseselection";
    }
}
