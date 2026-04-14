package com.example.attendancesystem.repository;

import com.example.attendancesystem.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(String studentId);

    List<Attendance> findByCourseId(String courseId);

    List<Attendance> findByStudentIdAndCourseId(String studentId, String courseId);
}
