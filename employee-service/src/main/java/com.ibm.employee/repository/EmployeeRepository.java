package com.ibm.employee.repository;

import com.ibm.employee.entity.Employee;
import com.ibm.employee.entity.enums.EmploymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository
        extends MongoRepository<Employee, String> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    List<Employee> findByDepartmentId(String departmentId);

    List<Employee> findByDesignationId(String designationId);

    List<Employee> findByManagerId(String managerId);

    List<Employee> findByEmploymentStatus(EmploymentStatus status);
    
    Page<Employee> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Employee> findByIdAndIsDeletedFalse(String id);

    Optional<Employee> findByEmployeeCodeAndIsDeletedFalse(String employeeCode);

    List<Employee> findByDepartmentIdAndIsDeletedFalse(String departmentId);

    List<Employee> findByDesignationIdAndIsDeletedFalse(String designationId);

    List<Employee> findByManagerIdAndIsDeletedFalse(String managerId);

    List<Employee> findByEmploymentStatusAndIsDeletedFalse(EmploymentStatus status);
}