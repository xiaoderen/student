package com.example.attendancesystem.repository;

import com.example.attendancesystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    List<Student> findByStudentIdContaining(String studentId);

    List<Student> findByStudentNameContaining(String studentName);

    List<Student> findByStudentIdContainingAndStudentNameContaining(String studentId, String studentName);
}
