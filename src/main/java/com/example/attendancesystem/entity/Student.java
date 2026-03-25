package com.example.attendancesystem.entity;

public class Student {
    private String studentId;
    private String name;
    private String className;
    private String major;

    public Student() {}
    public Student(String studentId, String name, String className, String major) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
        this.major = major;
    }

    // Getters and Setters...
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}