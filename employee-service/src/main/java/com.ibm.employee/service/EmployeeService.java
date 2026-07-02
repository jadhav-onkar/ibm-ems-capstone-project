package com.ibm.employee.service;

import com.ibm.employee.dto.request.CreateEmployeeRequest;
import com.ibm.employee.dto.response.EmployeeResponse;
import com.ibm.employee.dto.request.UpdateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateStatusRequest;
import com.ibm.employee.entity.enums.EmploymentStatus;

import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(String id);
    
    EmployeeResponse updateEmployee(
            String id,
            UpdateEmployeeRequest request);

    void deleteEmployee(String id);

    EmployeeResponse getEmployeeByCode(String employeeCode);

    List<EmployeeResponse> getByDepartment(String departmentId);

    List<EmployeeResponse> getByDesignation(String designationId);

    List<EmployeeResponse> getByManager(String managerId);

    List<EmployeeResponse> getByStatus(EmploymentStatus status);

    EmployeeResponse updateStatus(
            String id,
            UpdateStatusRequest request);

    Page<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction);
    
    List<EmployeeResponse> getAllEmployees();
}