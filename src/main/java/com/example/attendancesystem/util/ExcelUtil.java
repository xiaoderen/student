package com.example.attendancesystem.util;

import com.example.attendancesystem.dto.CourseSelectionImportDTO;
import com.example.attendancesystem.dto.StudentImportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static List<StudentImportDTO> parseExcel(MultipartFile file) throws Exception {
        List<StudentImportDTO> students = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            StudentImportDTO dto = new StudentImportDTO();

            Cell studentIdCell = row.getCell(0);
            if (studentIdCell != null) {
                dto.setStudentId(getCellValue(studentIdCell));
            }

            Cell nameCell = row.getCell(1);
            if (nameCell != null) {
                dto.setStudentName(getCellValue(nameCell));
            }

            Cell genderCell = row.getCell(2);
            if (genderCell != null) {
                dto.setGender(getCellValue(genderCell));
            }

            Cell classCell = row.getCell(3);
            if (classCell != null) {
                dto.setClassName(getCellValue(classCell));
            }

            if (dto.getStudentId() != null && !dto.getStudentId().isEmpty() &&
                    dto.getStudentName() != null && !dto.getStudentName().isEmpty()) {
                students.add(dto);
            }
        }

        workbook.close();
        inputStream.close();

        return students;
    }

    public static List<CourseSelectionImportDTO> parseCourseSelectionExcel(MultipartFile file) throws Exception {
        List<CourseSelectionImportDTO> selections = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            CourseSelectionImportDTO dto = new CourseSelectionImportDTO();

            Cell studentIdCell = row.getCell(0);
            if (studentIdCell != null) {
                dto.setStudentId(getCellValue(studentIdCell));
            }

            Cell courseIdCell = row.getCell(1);
            if (courseIdCell != null) {
                dto.setCourseId(getCellValue(courseIdCell));
            }

            if (dto.getStudentId() != null && !dto.getStudentId().isEmpty() &&
                    dto.getCourseId() != null && !dto.getCourseId().isEmpty()) {
                selections.add(dto);
            }
        }

        workbook.close();
        inputStream.close();

        return selections;
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.valueOf((long) value);
                    }
                    return String.valueOf(value);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
