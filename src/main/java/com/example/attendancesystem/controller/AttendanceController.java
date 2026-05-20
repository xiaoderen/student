package com.example.attendancesystem.controller;

import com.example.attendancesystem.dto.CheckInRequest;
import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.Course;
import com.example.attendancesystem.entity.User;
import com.example.attendancesystem.repository.AttendanceRepository;
import com.example.attendancesystem.repository.CourseRepository;
import com.example.attendancesystem.service.impl.AttendanceServiceImpl;
import com.example.attendancesystem.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceServiceImpl attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id", "studentId", "courseId", "checkInTime",
            "seatRow", "seatCol", "status", "ip", "createTime"
    );

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("接口正常");
    }

    @GetMapping("/available-courses")
    public Result<List<Course>> getAvailableCourses(HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                return Result.error("请先登录");
            }

            List<Course> allCourses = courseRepository.findAll();

            List<Course> availableCourses = new ArrayList<>();
            for (Course course : allCourses) {
                boolean hasChecked = attendanceService.hasCheckedInToday(currentUser.getUsername(), course.getCourseId());

                if (!hasChecked) {
                    availableCourses.add(course);
                }
            }

            return Result.success(availableCourses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/checkin")
    public Result<Attendance> checkIn(@RequestBody CheckInRequest request, HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                return Result.error("请先登录");
            }

            String ip = httpRequest.getRemoteAddr();

            Attendance attendance = attendanceService.checkIn(
                    currentUser.getUsername(),
                    request.getCourseId(),
                    request.getSeatRow(),
                    request.getSeatCol(),
                    ip
            );

            return Result.success(attendance);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/records")
    public Result<Page<Attendance>> getRecords(
            HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                return Result.error("请先登录");
            }

            Pageable pageable = attendanceService.createPageable(page, size, "checkInTime", "DESC");

            if (startDate != null && endDate != null) {
                LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
                LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
                Page<Attendance> records = attendanceService.getAttendancesByStudentIdAndDateRange(
                        currentUser.getUsername(), start, end, pageable);
                return Result.success(records);
            } else {
                Page<Attendance> records = attendanceService.getAttendancesByStudentId(currentUser.getUsername(), pageable);
                return Result.success(records);
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void exportAttendance(HttpServletRequest httpRequest, HttpServletResponse response,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate) throws IOException {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                response.sendRedirect("/login");
                return;
            }

            List<Attendance> records;
            if (startDate != null && endDate != null) {
                LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
                LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
                records = attendanceRepository.findByStudentIdAndCheckInTimeBetween(
                        currentUser.getUsername(), start, end);
            } else {
                records = attendanceRepository.findByStudentId(currentUser.getUsername());
            }

            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=attendance_records.csv");

            response.getWriter().write("课程ID,打卡时间,座位,状态,IP地址\n");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Attendance record : records) {
                response.getWriter().write(String.format("%s,%s,%d排%d座,%s,%s\n",
                        record.getCourseId(),
                        record.getCheckInTime().format(formatter),
                        record.getSeatRow(),
                        record.getSeatCol(),
                        record.getStatus(),
                        record.getIp()));
            }
            response.getWriter().flush();
        } catch (Exception e) {
            response.sendRedirect("/error");
        }
    }

    @GetMapping("/today")
    public Result<List<Attendance>> getTodayAttendances(HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                return Result.error("请先登录");
            }

            List<Attendance> attendances = attendanceService.getTodayAttendancesByStudentId(currentUser.getUsername());
            return Result.success(attendances);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/checked-today/{courseId}")
    public Result<Boolean> hasCheckedInToday(@PathVariable String courseId, HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                return Result.error("请先登录");
            }

            boolean checked = attendanceService.hasCheckedInToday(currentUser.getUsername(), courseId);
            return Result.success(checked);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<Page<Attendance>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return Result.error("不支持的排序字段: " + sortBy);
        }

        Pageable pageable = attendanceService.createPageable(page, size, sortBy, direction);
        return Result.success(attendanceService.getAllAttendances(pageable));
    }

    @GetMapping("/student/{studentId}")
    public Result<Page<Attendance>> getByStudentId(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = attendanceService.createPageable(page, size, "checkInTime", "DESC");
        return Result.success(attendanceService.getAttendancesByStudentId(studentId, pageable));
    }
}
