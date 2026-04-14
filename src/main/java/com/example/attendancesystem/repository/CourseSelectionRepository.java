package com.example.attendancesystem.repository;

import com.example.attendancesystem.entity.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Long> {

    List<CourseSelection> findByStudentId(String studentId);

    List<CourseSelection> findByCourseId(String courseId);

    Optional<CourseSelection> findByStudentIdAndCourseId(String studentId, String courseId);
}
