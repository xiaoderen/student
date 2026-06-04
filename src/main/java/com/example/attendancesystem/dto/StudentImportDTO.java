package com.example.attendancesystem.dto;

public class StudentImportDTO {
    private String studentId;
    private String studentName;
    private String gender;
    private String className;

    public StudentImportDTO() {}

    public StudentImportDTO(String studentId, String studentName, String gender, String className) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.className = className;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
