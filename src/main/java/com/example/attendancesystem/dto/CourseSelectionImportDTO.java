package com.example.attendancesystem.dto;

public class CourseSelectionImportDTO {
    private String studentId;
    private String courseId;

    public CourseSelectionImportDTO() {}

    public CourseSelectionImportDTO(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
}
