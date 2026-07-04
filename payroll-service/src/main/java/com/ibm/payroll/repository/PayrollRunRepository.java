package com.ibm.payroll.repository;

import com.ibm.payroll.entity.PayrollRun;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PayrollRunRepository extends MongoRepository<PayrollRun, String> {

    Optional<PayrollRun> findByPeriod(String period);
}
