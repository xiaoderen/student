package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {

    Page<Attendance> getAllAttendances(Pageable pageable);

    Page<Attendance> getAttendancesByStudentId(String studentId, Pageable pageable);

    Page<Attendance> getAttendancesByCourseId(String courseId, Pageable pageable);

    Page<Attendance> getAttendancesByStudentIdAndCourseId(String studentId, String courseId, Pageable pageable);

    Attendance createAttendance(Attendance attendance);

    Attendance getAttendanceById(Long id);
}
