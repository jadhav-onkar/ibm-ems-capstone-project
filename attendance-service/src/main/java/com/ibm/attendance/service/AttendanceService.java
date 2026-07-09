package com.ibm.attendance.service;

import java.util.List;

import com.ibm.attendance.dto.AttendanceHistoryResponse;
import com.ibm.attendance.dto.AttendanceRequest;
import com.ibm.attendance.dto.AttendanceResponse;
import com.ibm.attendance.dto.CheckInRequest;
import com.ibm.attendance.dto.CheckOutRequest;
import com.ibm.attendance.dto.MonthlyReportResponse;

public interface AttendanceService {

    AttendanceResponse createAttendance(AttendanceRequest request);

    List<AttendanceResponse> getAllAttendance();
    
    AttendanceResponse getAttendanceById(String id);
    
    AttendanceResponse updateAttendance(String id, AttendanceRequest request);
    
    AttendanceResponse checkIn(CheckInRequest request);

    AttendanceResponse checkOut(CheckOutRequest request);
    
//    List<AttendanceResponse> getAttendanceHistory(String employeeId);
    
    List<AttendanceHistoryResponse> getAttendanceHistory(String employeeId);
    
    MonthlyReportResponse getMonthlyReport(String employeeId, int month, int year);	
    
    AttendanceResponse getAttendanceByEmployeeId(String employeeId);
    
    
    void deleteAttendance(String id);
}
