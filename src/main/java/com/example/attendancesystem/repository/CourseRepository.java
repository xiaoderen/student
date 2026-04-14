package com.example.attendancesystem.repository;

import com.example.attendancesystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByClassName(String className);
}
