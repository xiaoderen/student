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

    @PutMapping("/update")
    public Result<String> update(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
            return Result.success("学生信息更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return Result.success("学生删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
