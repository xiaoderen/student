package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Student;

public interface StudentService {
    // 创建学生
    String createStudent(Student student);
    // 根据学号查询学生
    Student getStudentById(String studentId);
}