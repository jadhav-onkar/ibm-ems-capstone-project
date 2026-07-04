package com.ibm.payroll.repository;

import com.ibm.payroll.entity.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {

    List<Goal> findByEmployeeId(String employeeId);
}
