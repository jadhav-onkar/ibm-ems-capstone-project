package com.ibm.payroll.repository;

import com.ibm.payroll.entity.SalaryHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SalaryHistoryRepository extends MongoRepository<SalaryHistory, String> {

    List<SalaryHistory> findByEmployeeIdOrderByRecordedAtDesc(String employeeId);
}
