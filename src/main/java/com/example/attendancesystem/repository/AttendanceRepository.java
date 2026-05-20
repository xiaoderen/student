package com.example.attendancesystem.repository;

import com.example.attendancesystem.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(String studentId);

    List<Attendance> findByCourseId(String courseId);

    List<Attendance> findByStudentIdAndCourseId(String studentId, String courseId);

    Page<Attendance> findByStudentId(String studentId, Pageable pageable);

    Page<Attendance> findByCourseId(String courseId, Pageable pageable);

    Page<Attendance> findByStudentIdAndCourseId(String studentId, String courseId, Pageable pageable);

    Page<Attendance> findAll(Pageable pageable);

    List<Attendance> findByStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsByStudentIdAndCourseIdAndCheckInTimeBetween(String studentId, String courseId, LocalDateTime startTime, LocalDateTime endTime);

    Page<Attendance> findByStudentIdAndCheckInTimeBetween(String studentId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
