package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.service.StudentService;
import com.example.attendancesystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public Result<String> create(@RequestBody Student student) {
        return Result.success(studentService.createStudent(student));
    }

    @GetMapping("/{studentId}")
    public Result<Student> getByStudentId(@PathVariable String studentId) {
        return Result.success(studentService.getStudentByStudentId(studentId));
    }

    @GetMapping("/list")
    public Result<List<Student>> list() {
        return Result.success(studentService.getAllStudents());
    }
}