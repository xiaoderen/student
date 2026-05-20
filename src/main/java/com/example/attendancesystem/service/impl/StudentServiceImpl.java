package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.repository.StudentRepository;
import com.example.attendancesystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public String createStudent(Student student) {
        if (student.getStudentName() == null || student.getStudentName().isEmpty()) {
            throw new RuntimeException("学生姓名不能为空");
        }
        if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        if (student.getClassName() == null || student.getClassName().isEmpty()) {
            throw new RuntimeException("班级不能为空");
        }

        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("学号已存在：" + student.getStudentId());
        }

        student.setCreateTime(LocalDateTime.now());
        studentRepository.save(student);
        return "学生创建成功，学号：" + student.getStudentId();
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).orElse(null);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void updateStudent(Student student) {
        if (student.getId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setGender(student.getGender());
        existingStudent.setClassName(student.getClassName());

        studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("学生不存在");
        }
        studentRepository.deleteById(id);
    }
}
