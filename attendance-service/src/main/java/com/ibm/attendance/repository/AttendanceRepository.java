package com.ibm.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);

    List<Attendance> findByEmployeeId(String employeeId);
    
    List<Attendance> findByEmployeeIdOrderByDateDesc(String employeeId);
    
    Optional<Attendance> findTopByEmployeeIdOrderByDateDesc(String employeeId);


    
    

}
