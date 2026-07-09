package com.ibm.attendance.controller;

import com.ibm.attendance.dto.AttendanceHistoryResponse;
import com.ibm.attendance.dto.AttendanceRequest;
import com.ibm.attendance.dto.AttendanceResponse;
import com.ibm.attendance.dto.CheckInRequest;
import com.ibm.attendance.dto.CheckOutRequest;
import com.ibm.attendance.dto.MonthlyReportResponse;
import com.ibm.attendance.service.AttendanceService;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Test API
    @GetMapping("/test")
    public String test() {
        return "Attendance Service is Working!";
    }

    // Create Attendance API
    @PostMapping
    public AttendanceResponse createAttendance(
            @Valid @RequestBody AttendanceRequest request) {

        return attendanceService.createAttendance(request);
    }
    
    @GetMapping
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceService.getAllAttendance();
    }
    @GetMapping("/{id}")
    public AttendanceResponse getAttendanceById(@PathVariable String id) {

        return attendanceService.getAttendanceById(id);
    }
    
    @PutMapping("/{id}")
    public AttendanceResponse updateAttendance(
            @PathVariable String id,
            @Valid @RequestBody AttendanceRequest request) {

        return attendanceService.updateAttendance(id, request);
    }
    @DeleteMapping("/{id}")
    public String deleteAttendance(@PathVariable String id) {

        attendanceService.deleteAttendance(id);

        return "Attendance deleted successfully.";
    }
    
    @PostMapping("/checkin")
    public AttendanceResponse checkIn(
            @Valid @RequestBody CheckInRequest request) {

        return attendanceService.checkIn(request);
    }
    
    @PostMapping("/checkout")
    public AttendanceResponse checkOut(
            @Valid @RequestBody CheckOutRequest request) {

        return attendanceService.checkOut(request);
    }
    
    @GetMapping("/history/{employeeId}")
    public List<AttendanceHistoryResponse> getHistory(@PathVariable String employeeId) {
        return attendanceService.getAttendanceHistory(employeeId);
    }
    
    @GetMapping("/monthly/{employeeId}")
    public MonthlyReportResponse getMonthlyReport(
            @PathVariable String employeeId,
            @RequestParam int month,
            @RequestParam int year) {

        return attendanceService.getMonthlyReport(employeeId, month, year);
    }
    
    @GetMapping("/employee/{employeeId}")
    public AttendanceResponse getAttendanceByEmployeeId(
            @PathVariable String employeeId) {

        return attendanceService.getAttendanceByEmployeeId(employeeId);
    }
    
}
