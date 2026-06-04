package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StudentService {
    String createStudent(Student student);
    Student getStudentByStudentId(String studentId);
    Student getStudentById(Long id);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(Long id);
    Map<String, Object> importStudentsFromExcel(MultipartFile file);
    Map<String, Long> getGenderStatistics();
}
