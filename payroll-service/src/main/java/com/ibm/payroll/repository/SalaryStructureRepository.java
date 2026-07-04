package com.ibm.payroll.repository;

import com.ibm.payroll.entity.SalaryStructure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SalaryStructureRepository extends MongoRepository<SalaryStructure, String> {

    Optional<SalaryStructure> findTopByEmployeeIdOrderByEffectiveDateDesc(String employeeId);
}
