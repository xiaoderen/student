package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.Course;
import com.example.attendancesystem.repository.AttendanceRepository;
import com.example.attendancesystem.repository.CourseRepository;
import com.example.attendancesystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final int LATE_MINUTES = 10;
    private static final int EARLY_CHECKIN_MINUTES = 15;
    private static final int LATE_CHECKIN_MINUTES = 30;

    private static final String DEFAULT_SORT_FIELD = "checkInTime";

    @Override
    public Page<Attendance> getAllAttendances(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    public Page<Attendance> getAttendancesByStudentId(String studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable);
    }

    @Override
    public Page<Attendance> getAttendancesByCourseId(String courseId, Pageable pageable) {
        return attendanceRepository.findByCourseId(courseId, pageable);
    }

    @Override
    public Page<Attendance> getAttendancesByStudentIdAndCourseId(String studentId, String courseId, Pageable pageable) {
        return attendanceRepository.findByStudentIdAndCourseId(studentId, courseId, pageable);
    }

    @Override
    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @Override
    public Attendance checkIn(String studentId, String courseId, Integer seatRow, Integer seatCol, String ip) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        if (course.getStartTime() == null || course.getEndTime() == null) {
            throw new RuntimeException("课程未设置时间");
        }

        if (hasCheckedInToday(studentId, courseId)) {
            throw new RuntimeException("今天已经打过卡了");
        }

        LocalDateTime now = LocalDateTime.now();

        if (!isWithinCheckInTime(course, now)) {
            throw new RuntimeException("不在打卡时间范围内（开课前 15 分钟~开课后 30 分钟）");
        }

        Attendance.AttendanceStatus status = calculateStatus(course, now);

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setCheckInTime(now);
        attendance.setSeatRow(seatRow);
        attendance.setSeatCol(seatCol);
        attendance.setStatus(status);
        attendance.setIp(ip);
        attendance.setCreateTime(now);

        return attendanceRepository.save(attendance);
    }

    private boolean isWithinCheckInTime(Course course, LocalDateTime checkInTime) {
        LocalTime checkIn = checkInTime.toLocalTime();
        LocalTime courseStart = course.getStartTime();

        LocalTime earliestCheckIn = courseStart.minusMinutes(EARLY_CHECKIN_MINUTES);
        LocalTime latestCheckIn = courseStart.plusMinutes(LATE_CHECKIN_MINUTES);

        return !checkIn.isBefore(earliestCheckIn) && !checkIn.isAfter(latestCheckIn);
    }

    private Attendance.AttendanceStatus calculateStatus(Course course, LocalDateTime checkInTime) {
        LocalTime checkIn = checkInTime.toLocalTime();
        LocalTime courseStart = course.getStartTime();
        LocalTime lateThreshold = courseStart.plusMinutes(LATE_MINUTES);

        if (checkIn.isAfter(lateThreshold)) {
            return Attendance.AttendanceStatus.LATE;
        }
        return Attendance.AttendanceStatus.NORMAL;
    }

    @Override
    public List<Attendance> getTodayAttendancesByStudentId(String studentId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return attendanceRepository.findByStudentIdAndCheckInTimeBetween(studentId, startOfDay, endOfDay);
    }

    @Override
    public boolean hasCheckedInToday(String studentId, String courseId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return attendanceRepository.existsByStudentIdAndCourseIdAndCheckInTimeBetween(
                studentId, courseId, startOfDay, endOfDay);
    }

    @Override
    public Page<Attendance> getAttendancesByStudentIdAndDateRange(String studentId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return attendanceRepository.findByStudentIdAndCheckInTimeBetween(studentId, startTime, endTime, pageable);
    }

    public Pageable createPageable(int page, int size, String sortBy, String direction) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = DEFAULT_SORT_FIELD;
        }

        Sort sort = "ASC".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    public Pageable createPageableWithMultiSort(int page, int size, String[] sortFields, String[] directions) {
        if (sortFields == null || sortFields.length == 0) {
            sortFields = new String[]{DEFAULT_SORT_FIELD};
            directions = new String[]{"DESC"};
        }

        Sort.Order[] orders = new Sort.Order[sortFields.length];
        for (int i = 0; i < sortFields.length; i++) {
            String field = sortFields[i];
            boolean isAsc = i < directions.length && "ASC".equalsIgnoreCase(directions[i]);
            orders[i] = isAsc ? Sort.Order.asc(field) : Sort.Order.desc(field);
        }

        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }
}
