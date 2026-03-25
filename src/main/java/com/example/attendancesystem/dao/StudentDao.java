package com.example.attendancesystem.dao;

import com.example.attendancesystem.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// 标记为Spring的数据访问层组件
@Repository
public class StudentDao {

    // 注入Spring提供的JdbcTemplate，用于操作数据库
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 插入学生数据到数据库
     * @param student 要插入的学生对象
     */
    public void insert(Student student) {
        // 插入SQL，?是占位符，防止SQL注入
        String sql = "INSERT INTO student (student_id, name, class_name) VALUES (?, ?, ?)";
        // 执行更新操作，按顺序传入占位符的参数
        jdbcTemplate.update(sql,
                student.getStudentId(),
                student.getName(),
                student.getClassName());
    }

    /**
     * 根据学号查询学生信息
     * @param studentId 学生学号
     * @return 对应的学生对象
     */
    public Student findById(String studentId) {
        // 查询SQL
        String sql = "SELECT * FROM student WHERE student_id = ?";
        // 执行查询，BeanPropertyRowMapper会自动把数据库字段映射到Student对象的属性
        return jdbcTemplate.queryForObject(sql,
                new BeanPropertyRowMapper<>(Student.class),
                studentId);
    }
}