package com.ibm.payroll.repository;

import com.ibm.payroll.entity.EmployeeRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeRatingRepository extends MongoRepository<EmployeeRating, String> {

    List<EmployeeRating> findByEmployeeId(String employeeId);
}
