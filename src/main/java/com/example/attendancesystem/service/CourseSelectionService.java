package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.CourseSelection;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseSelectionService {
    Map<String, Object> importCourseSelectionsFromExcel(MultipartFile file);
    List<CourseSelection> getSelectionsByStudentId(String studentId);
    List<CourseSelection> getSelectionsByCourseId(String courseId);
    void deleteSelection(Long id);
}
