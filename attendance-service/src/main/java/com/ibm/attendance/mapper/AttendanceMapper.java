package com.ibm.attendance.mapper;

import com.ibm.attendance.dto.AttendanceResponse;
import com.ibm.attendance.entity.Attendance;

public class AttendanceMapper {

    public static AttendanceResponse toResponse(Attendance attendance) {

        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setStatus(attendance.getStatus());

        return response;
    }

}
