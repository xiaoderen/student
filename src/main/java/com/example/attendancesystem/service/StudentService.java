package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Student;
import java.util.List;

public interface StudentService {
    String createStudent(Student student);
    Student getStudentByStudentId(String studentId);
    Student getStudentById(Long id);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(Long id);
}
