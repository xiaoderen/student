package com.example.attendancesystem.service.impl;

import com.example.attendance_system.service.StudentService;
import com.example.attendancesystem.dao.StudentDao;
import com.example.attendancesystem.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public String createStudent(Student student) {
        // 业务逻辑：校验学生姓名
        if (student.getName() == null || student.getName().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        // 调用Dao层执行插入
        studentDao.insert(student);
        return "创建成功";
    }

    @Override
    public Student getStudentById(String studentId) {
        // 调用Dao层执行查询
        return studentDao.findById(studentId);
    }
}