package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Student;
import java.util.List;

public interface StudentService {
    // 创建学生
    String createStudent(Student student);
    // 根据学号查询学生
    Student getStudentByStudentId(String studentId);
    // 查询所有学生
    List<Student> getAllStudents();
}