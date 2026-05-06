package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.service.impl.AttendanceServiceImpl;
import com.example.attendancesystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceServiceImpl attendanceService;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id", "studentId", "courseId", "checkInTime",
            "seatRow", "seatCol", "status", "ip", "createTime"
    );

    @PostMapping("/create")
    public Result<Attendance> create(@RequestBody Attendance attendance) {
        Attendance created = attendanceService.createAttendance(attendance);
        return Result.success(created);
    }

    @GetMapping("/{id}")
    public Result<Attendance> getById(@PathVariable Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        if (attendance != null) {
            return Result.success(attendance);
        }
        return Result.error("考勤记录不存在");
    }

    @GetMapping("/list")
    public Result<Page<Attendance>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return Result.error("不支持的排序字段: " + sortBy + "。支持的字段: " + ALLOWED_SORT_FIELDS);
        }

        Pageable pageable = attendanceService.createPageable(page, size, sortBy, direction);
        Page<Attendance> attendances = attendanceService.getAllAttendances(pageable);
        return Result.success(attendances);
    }

    @GetMapping("/student/{studentId}")
    public Result<Page<Attendance>> getByStudentId(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return Result.error("不支持的排序字段: " + sortBy + "。支持的字段: " + ALLOWED_SORT_FIELDS);
        }

        Pageable pageable = attendanceService.createPageable(page, size, sortBy, direction);
        Page<Attendance> attendances = attendanceService.getAttendancesByStudentId(studentId, pageable);
        return Result.success(attendances);
    }

    @GetMapping("/course/{courseId}")
    public Result<Page<Attendance>> getByCourseId(
            @PathVariable String courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return Result.error("不支持的排序字段: " + sortBy + "。支持的字段: " + ALLOWED_SORT_FIELDS);
        }

        Pageable pageable = attendanceService.createPageable(page, size, sortBy, direction);
        Page<Attendance> attendances = attendanceService.getAttendancesByCourseId(courseId, pageable);
        return Result.success(attendances);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public Result<Page<Attendance>> getByStudentIdAndCourseId(
            @PathVariable String studentId,
            @PathVariable String courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return Result.error("不支持的排序字段: " + sortBy + "。支持的字段: " + ALLOWED_SORT_FIELDS);
        }

        Pageable pageable = attendanceService.createPageable(page, size, sortBy, direction);
        Page<Attendance> attendances = attendanceService.getAttendancesByStudentIdAndCourseId(studentId, courseId, pageable);
        return Result.success(attendances);
    }

    @GetMapping("/list/multi-sort")
    public Result<Page<Attendance>> listWithMultiSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sortFields,
            @RequestParam(defaultValue = "DESC") String directions) {

        String[] fields = sortFields.split(",");
        String[] dirs = directions.split(",");

        for (String field : fields) {
            if (!ALLOWED_SORT_FIELDS.contains(field.trim())) {
                return Result.error("不支持的排序字段: " + field + "。支持的字段: " + ALLOWED_SORT_FIELDS);
            }
        }

        Pageable pageable = attendanceService.createPageableWithMultiSort(page, size, fields, dirs);
        Page<Attendance> attendances = attendanceService.getAllAttendances(pageable);
        return Result.success(attendances);
    }
}
