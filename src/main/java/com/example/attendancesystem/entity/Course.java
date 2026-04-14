package com.example.attendancesystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "course_id", length = 20)
    private String courseId;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "class_name", nullable = false, length = 50)
    private String className;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "classroom_name", length = 50)
    private String classroomName;

    @Column(name = "seat_rows")
    private Integer seatRows;

    @Column(name = "seat_cols")
    private Integer seatCols;

    @Column(name = "exclude_seats", length = 200)
    private String excludeSeats;

    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Column(name = "start_week")
    private Integer startWeek;

    @Column(name = "end_week")
    private Integer endWeek;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    public Course() {}

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getClassroomName() { return classroomName; }
    public void setClassroomName(String classroomName) { this.classroomName = classroomName; }
    public Integer getSeatRows() { return seatRows; }
    public void setSeatRows(Integer seatRows) { this.seatRows = seatRows; }
    public Integer getSeatCols() { return seatCols; }
    public void setSeatCols(Integer seatCols) { this.seatCols = seatCols; }
    public String getExcludeSeats() { return excludeSeats; }
    public void setExcludeSeats(String excludeSeats) { this.excludeSeats = excludeSeats; }
    public Integer getWeekday() { return weekday; }
    public void setWeekday(Integer weekday) { this.weekday = weekday; }
    public Integer getStartWeek() { return startWeek; }
    public void setStartWeek(Integer startWeek) { this.startWeek = startWeek; }
    public Integer getEndWeek() { return endWeek; }
    public void setEndWeek(Integer endWeek) { this.endWeek = endWeek; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
