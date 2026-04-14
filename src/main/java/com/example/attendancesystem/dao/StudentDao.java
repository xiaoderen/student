package com.example.attendancesystem.dao;

import com.example.attendancesystem.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Student student) {
        String sql = "INSERT INTO student (student_id, student_name, gender, class_name,create_time) VALUES (?, ?, ?, ?,?)";
        jdbcTemplate.update(sql,
                student.getStudentId(),
                student.getStudentName(),
                student.getGender(),
                student.getClassName(),
        LocalDateTime.now());
    }
    public Student findByStudentId(String studentId) {
        String sql = "SELECT * FROM student WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql,
                new BeanPropertyRowMapper<>(Student.class),
                studentId);
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class));
    }
}