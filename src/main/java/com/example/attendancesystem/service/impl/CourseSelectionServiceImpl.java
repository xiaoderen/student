package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.dto.CourseSelectionImportDTO;
import com.example.attendancesystem.entity.CourseSelection;
import com.example.attendancesystem.repository.CourseRepository;
import com.example.attendancesystem.repository.CourseSelectionRepository;
import com.example.attendancesystem.repository.StudentRepository;
import com.example.attendancesystem.service.CourseSelectionService;
import com.example.attendancesystem.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Map<String, Object> importCourseSelectionsFromExcel(MultipartFile file) {
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

            List<CourseSelectionImportDTO> importList = ExcelUtil.parseCourseSelectionExcel(file);

            if (importList.isEmpty()) {
                result.put("success", false);
                result.put("message", "Excel文件中没有有效数据");
                return result;
            }

            int successCount = 0;
            int failCount = 0;
            StringBuilder failMessages = new StringBuilder();

            for (CourseSelectionImportDTO dto : importList) {
                try {
                    if (dto.getStudentId() == null || dto.getStudentId().isEmpty()) {
                        failCount++;
                        failMessages.append("第").append(successCount + failCount + 2).append("行：学号不能为空；");
                        continue;
                    }

                    if (dto.getCourseId() == null || dto.getCourseId().isEmpty()) {
                        failCount++;
                        failMessages.append("第").append(successCount + failCount + 2).append("行：课程号不能为空；");
                        continue;
                    }

                    if (!studentRepository.existsByStudentId(dto.getStudentId())) {
                        failCount++;
                        failMessages.append("学号 ").append(dto.getStudentId()).append(" 不存在；");
                        continue;
                    }

                    if (!courseRepository.existsById(dto.getCourseId())) {
                        failCount++;
                        failMessages.append("课程号 ").append(dto.getCourseId()).append(" 不存在；");
                        continue;
                    }

                    if (courseSelectionRepository.findByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId()).isPresent()) {
                        failCount++;
                        failMessages.append("学号 ").append(dto.getStudentId()).append(" 已选择课程 ").append(dto.getCourseId()).append("；");
                        continue;
                    }

                    CourseSelection selection = new CourseSelection();
                    selection.setStudentId(dto.getStudentId());
                    selection.setCourseId(dto.getCourseId());
                    selection.setSelectTime(LocalDateTime.now());

                    courseSelectionRepository.save(selection);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    failMessages.append("第").append(successCount + failCount + 1).append("行: ")
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
    public List<CourseSelection> getSelectionsByStudentId(String studentId) {
        return courseSelectionRepository.findByStudentId(studentId);
    }

    @Override
    public List<CourseSelection> getSelectionsByCourseId(String courseId) {
        return courseSelectionRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteSelection(Long id) {
        if (!courseSelectionRepository.existsById(id)) {
            throw new RuntimeException("选课记录不存在");
        }
        courseSelectionRepository.deleteById(id);
    }
}
