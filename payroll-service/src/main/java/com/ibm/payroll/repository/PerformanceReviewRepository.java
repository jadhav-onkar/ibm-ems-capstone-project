package com.ibm.payroll.repository;

import com.ibm.payroll.entity.PerformanceReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PerformanceReviewRepository extends MongoRepository<PerformanceReview, String> {

    List<PerformanceReview> findByEmployeeId(String employeeId);
}
