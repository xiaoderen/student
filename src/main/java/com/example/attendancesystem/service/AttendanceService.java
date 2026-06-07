package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceService {

    Page<Attendance> getAllAttendances(Pageable pageable);

    Page<Attendance> getAttendancesByStudentId(String studentId, Pageable pageable);

    Page<Attendance> getAttendancesByCourseId(String courseId, Pageable pageable);

    Page<Attendance> getAttendancesByStudentIdAndCourseId(String studentId, String courseId, Pageable pageable);

    Attendance createAttendance(Attendance attendance);

    Attendance getAttendanceById(Long id);

    Attendance checkIn(String studentId, String courseId, Integer seatRow, Integer seatCol, String ip);

    List<Attendance> getTodayAttendancesByStudentId(String studentId);

    boolean hasCheckedInToday(String studentId, String courseId);

    Page<Attendance> getAttendancesByStudentIdAndDateRange(String studentId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<Attendance> getAttendancesByCourseIdAndDateRange(String courseId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    List<Attendance> getAttendancesByCourseIdAndDateRange(String courseId, LocalDateTime startTime, LocalDateTime endTime);

    Page<Attendance> searchAttendances(String studentId, String courseId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
