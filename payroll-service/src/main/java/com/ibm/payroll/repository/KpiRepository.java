package com.ibm.payroll.repository;

import com.ibm.payroll.entity.KPI;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KpiRepository extends MongoRepository<KPI, String> {

    List<KPI> findByEmployeeId(String employeeId);
}
