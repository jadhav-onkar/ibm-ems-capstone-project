package com.ibm.payroll.repository;

import com.ibm.payroll.entity.Payslip;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PayslipRepository extends MongoRepository<Payslip, String> {

    Optional<Payslip> findByEmployeeIdAndPeriod(String employeeId, String period);
}
