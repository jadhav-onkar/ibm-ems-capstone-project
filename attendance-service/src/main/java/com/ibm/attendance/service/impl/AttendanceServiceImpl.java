package com.ibm.attendance.service.impl;

import org.springframework.stereotype.Service;

import com.ibm.attendance.dto.AttendanceHistoryResponse;
import com.ibm.attendance.dto.AttendanceRequest;
import com.ibm.attendance.dto.AttendanceResponse;
import com.ibm.attendance.dto.CheckInRequest;
import com.ibm.attendance.dto.CheckOutRequest;
import com.ibm.attendance.dto.MonthlyReportResponse;
import com.ibm.attendance.entity.Attendance;
import com.ibm.attendance.exception.AttendanceNotFoundException;
import com.ibm.attendance.repository.AttendanceRepository;
import com.ibm.attendance.service.AttendanceService;

import java.util.stream.Collectors;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // Constructor Injection
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {

        // Basic Validation
        if (request.getEmployeeId() == null || request.getEmployeeId().isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }

        // Convert Request DTO to Entity
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setStatus(request.getStatus());

        // Save into MongoDB
        Attendance savedAttendance = attendanceRepository.save(attendance);

        // Convert Entity to Response DTO
        AttendanceResponse response = new AttendanceResponse();
        response.setId(savedAttendance.getId());
        response.setEmployeeId(savedAttendance.getEmployeeId());
        response.setStatus(savedAttendance.getStatus());

        return response;
    }
    
    @Override
    public List<AttendanceResponse> getAllAttendance() {

        List<Attendance> attendanceList = attendanceRepository.findAll();

        List<AttendanceResponse> responseList = new ArrayList<>();

        for (Attendance attendance : attendanceList) {

            AttendanceResponse response = new AttendanceResponse();

            response.setId(attendance.getId());
            response.setEmployeeId(attendance.getEmployeeId());
            response.setStatus(attendance.getStatus());

            responseList.add(response);
        }

        return responseList;
    }
    
    @Override
    public AttendanceResponse getAttendanceById(String id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setStatus(attendance.getStatus());

        return response;
    }
    @Override
    public AttendanceResponse updateAttendance(String id, AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setStatus(request.getStatus());

        Attendance updatedAttendance = attendanceRepository.save(attendance);

        AttendanceResponse response = new AttendanceResponse();
        response.setId(updatedAttendance.getId());
        response.setEmployeeId(updatedAttendance.getEmployeeId());
        response.setStatus(updatedAttendance.getStatus());

        return response;
    }
    
    @Override
    public void deleteAttendance(String id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }
    
    @Override
    public AttendanceResponse checkIn(CheckInRequest request) {

        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance =
                attendanceRepository.findByEmployeeIdAndDate(
                        request.getEmployeeId(),
                        today);

        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Employee already checked in today.");
        }

        Attendance attendance = new Attendance();

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setDate(today);
        attendance.setCheckIn(LocalTime.now());
        attendance.setStatus("PRESENT");

        Attendance savedAttendance = attendanceRepository.save(attendance);

        AttendanceResponse response = new AttendanceResponse();

        response.setId(savedAttendance.getId());
        response.setEmployeeId(savedAttendance.getEmployeeId());
        response.setStatus(savedAttendance.getStatus());

        return response;
    }
    
    @Override
    public AttendanceResponse checkOut(CheckOutRequest request) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(request.getEmployeeId(), today)
                .orElseThrow(() -> new RuntimeException("Please check-in first before check-out"));

        // set checkout time
        LocalTime checkOutTime = LocalTime.now();
        attendance.setCheckOut(checkOutTime);

        // calculate working hours
        LocalTime checkInTime = attendance.getCheckIn();

        if (checkInTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            double hours = duration.toMinutes() / 60.0;
            attendance.setWorkingHours(hours);
        }

        attendance.setStatus("PRESENT");

        Attendance updated = attendanceRepository.save(attendance);

        AttendanceResponse response = new AttendanceResponse();
        response.setId(updated.getId());
        response.setEmployeeId(updated.getEmployeeId());
        response.setStatus(updated.getStatus());

        return response;
    }
    
    @Override
    public List<AttendanceHistoryResponse> getAttendanceHistory(String employeeId) {

        List<Attendance> attendanceList =
                attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId);

        return attendanceList.stream().map(attendance -> {

            AttendanceHistoryResponse response = new AttendanceHistoryResponse();

            response.setDate(attendance.getDate());
            response.setCheckIn(attendance.getCheckIn());
            response.setCheckOut(attendance.getCheckOut());
            response.setWorkingHours(attendance.getWorkingHours());
            response.setStatus(attendance.getStatus());

            return response;

        }).collect(Collectors.toList());
    }
    
    @Override
    public MonthlyReportResponse getMonthlyReport(String employeeId, int month, int year) {

        List<Attendance> records =
                attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId);

        // Filter by month/year
        List<Attendance> monthlyRecords = records.stream()
                .filter(a -> a.getDate() != null
                        && a.getDate().getMonthValue() == month
                        && a.getDate().getYear() == year)
                .toList();

        long presentDays = monthlyRecords.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        double totalHours = monthlyRecords.stream()
                .filter(a -> a.getWorkingHours() != null)
                .mapToDouble(Attendance::getWorkingHours)
                .sum();

        long workingDays = 30; // simplified assumption

        double expectedHours = workingDays * 8;

        double overtime = Math.max(0, totalHours - expectedHours);

        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setEmployeeId(employeeId);
        response.setMonth(month);
        response.setYear(year);

        response.setTotalWorkingDays(workingDays);
        response.setPresentDays(presentDays);
        response.setAbsentDays(workingDays - presentDays);

        response.setTotalWorkingHours(totalHours);
        response.setOvertimeHours(overtime);

        return response;
    }
    @Override
    public AttendanceResponse getAttendanceByEmployeeId(String employeeId) {

        Attendance attendance = attendanceRepository
                .findTopByEmployeeIdOrderByDateDesc(employeeId)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found"));

        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setStatus(attendance.getStatus());

        return response;
    }
    
}
