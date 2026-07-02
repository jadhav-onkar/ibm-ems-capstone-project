package com.ibm.employee.service.impl;

import java.util.List;

import com.ibm.employee.dto.request.CreateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateStatusRequest;

import com.ibm.employee.dto.response.EmployeeResponse;

import com.ibm.employee.entity.Employee;
import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.exception.DuplicateResourceException;
import com.ibm.employee.exception.ResourceNotFoundException;
import com.ibm.employee.mapper.EmployeeMapper;
import com.ibm.employee.repository.EmployeeRepository;
import com.ibm.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

//    Create employee
    @Override
    public EmployeeResponse createEmployee(
            CreateEmployeeRequest request) {

        if (employeeRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists");
        }

        Employee employee =
                employeeMapper.toEntity(request);

        employee = employeeRepository.save(employee);

        return employeeMapper.toResponse(employee);
    }

//    Get Employee By Id
    @Override
    public EmployeeResponse getEmployeeById(String id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        return employeeMapper.toResponse(employee);
    }
    
//    Update Employee
    @Override
    public EmployeeResponse updateEmployee(
            String id,
            UpdateEmployeeRequest request) {

        Employee employee =
                employeeRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        employeeMapper.updateEntity(employee, request);

        employee = employeeRepository.save(employee);

        return employeeMapper.toResponse(employee);
    }
    
//    Soft Delete
    @Override
    public void deleteEmployee(String id) {

        Employee employee =
                employeeRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        employee.setDeleted(true);

        employeeRepository.save(employee);
    }
    
//    Get by Employee Code
    @Override
    public EmployeeResponse getEmployeeByCode(
            String employeeCode) {

        Employee employee =
                employeeRepository
                        .findByEmployeeCodeAndIsDeletedFalse(
                                employeeCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        return employeeMapper.toResponse(employee);
    }
    
//    Get By Department
    @Override
    public List<EmployeeResponse> getByDepartment(
            String departmentId) {

        return employeeRepository
                .findByDepartmentIdAndIsDeletedFalse(
                        departmentId)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
    
//    Get By Designation
    @Override
    public List<EmployeeResponse> getByDesignation(
            String designationId) {

        return employeeRepository
                .findByDesignationIdAndIsDeletedFalse(
                        designationId)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
    
//    Get By Manager
    @Override
    public List<EmployeeResponse> getByManager(
            String managerId) {

        return employeeRepository
                .findByManagerIdAndIsDeletedFalse(
                        managerId)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
    
//    Get By Status
    @Override
    public List<EmployeeResponse> getByStatus(
            EmploymentStatus status) {

        return employeeRepository
                .findByEmploymentStatusAndIsDeletedFalse(
                        status)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
    
//    Update Status
    @Override
    public EmployeeResponse updateStatus(
            String id,
            UpdateStatusRequest request) {

        Employee employee =
                employeeRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        employee.setEmploymentStatus(
                request.getStatus());

        employee = employeeRepository.save(employee);

        return employeeMapper.toResponse(employee);
    }
    
//    Pagination + Sorting
    @Override
    public Page<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return employeeRepository
                .findAllByIsDeletedFalse(pageable)
                .map(employeeMapper::toResponse);
    }
    
//    Get All Employees
    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findByIsDeletedFalse()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}