package com.example.attendancesystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_selection")
public class CourseSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;

    @Column(name = "course_id", nullable = false, length = 20)
    private String courseId;

    @Column(name = "select_time")
    private LocalDateTime selectTime;

    public CourseSelection() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public LocalDateTime getSelectTime() { return selectTime; }
    public void setSelectTime(LocalDateTime selectTime) { this.selectTime = selectTime; }
}
