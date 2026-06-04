package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.dto.StudentImportDTO;
import com.example.attendancesystem.entity.Student;
import com.example.attendancesystem.repository.StudentRepository;
import com.example.attendancesystem.service.StudentService;
import com.example.attendancesystem.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public String createStudent(Student student) {
        if (student.getStudentName() == null || student.getStudentName().isEmpty()) {
            throw new RuntimeException("学生姓名不能为空");
        }
        if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        if (student.getClassName() == null || student.getClassName().isEmpty()) {
            throw new RuntimeException("班级不能为空");
        }

        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("学号已存在：" + student.getStudentId());
        }

        student.setCreateTime(LocalDateTime.now());
        studentRepository.save(student);
        return "学生创建成功，学号：" + student.getStudentId();
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).orElse(null);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void updateStudent(Student student) {
        if (student.getId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setGender(student.getGender());
        existingStudent.setClassName(student.getClassName());

        studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("学生不存在");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> importStudentsFromExcel(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (file == null || file.isEmpty()) {
                result.put("success", false);
                result.put("message", "文件不能为空");
                return result;
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.put("success", false);
                result.put("message", "请上传Excel文件(.xlsx或.xls)");
                return result;
            }

            List<StudentImportDTO> importList = ExcelUtil.parseExcel(file);

            if (importList.isEmpty()) {
                result.put("success", false);
                result.put("message", "Excel文件中没有有效数据");
                return result;
            }

            int successCount = 0;
            int failCount = 0;
            StringBuilder failMessages = new StringBuilder();

            for (StudentImportDTO dto : importList) {
                try {
                    if (studentRepository.existsByStudentId(dto.getStudentId())) {
                        failCount++;
                        failMessages.append("学号 ").append(dto.getStudentId()).append(" 已存在；");
                        continue;
                    }

                    Student student = new Student();
                    student.setStudentId(dto.getStudentId());
                    student.setStudentName(dto.getStudentName());
                    student.setGender(dto.getGender());
                    student.setClassName(dto.getClassName());
                    student.setCreateTime(LocalDateTime.now());

                    studentRepository.save(student);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    failMessages.append("学号 ").append(dto.getStudentId()).append(": ")
                            .append(e.getMessage()).append("；");
                }
            }

            result.put("success", true);
            result.put("message", String.format("导入完成！成功：%d条，失败：%d条", successCount, failCount));
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            if (failMessages.length() > 0) {
                result.put("failMessages", failMessages.toString());
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Long> getGenderStatistics() {
        List<Student> allStudents = studentRepository.findAll();

        Map<String, Long> statistics = new HashMap<>();
        long maleCount = allStudents.stream()
                .filter(s -> "男".equals(s.getGender()))
                .count();
        long femaleCount = allStudents.stream()
                .filter(s -> "女".equals(s.getGender()))
                .count();

        statistics.put("male", maleCount);
        statistics.put("female", femaleCount);

        return statistics;
    }
}
