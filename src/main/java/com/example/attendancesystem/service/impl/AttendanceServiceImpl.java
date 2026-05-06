package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.repository.AttendanceRepository;
import com.example.attendancesystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

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
