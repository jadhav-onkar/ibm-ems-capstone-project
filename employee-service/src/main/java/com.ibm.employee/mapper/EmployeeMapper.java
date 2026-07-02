package com.ibm.employee.mapper;

import com.ibm.employee.dto.request.CreateEmployeeRequest;
import com.ibm.employee.dto.response.EmployeeResponse;
import com.ibm.employee.entity.Employee;
import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.dto.request.UpdateEmployeeRequest;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeMapper {

    public Employee toEntity(CreateEmployeeRequest request) {

        return Employee.builder()
                .id(UUID.randomUUID().toString())
                .employeeCode(generateEmployeeCode())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .alternatePhoneNumber(
                        request.getAlternatePhoneNumber())
                .joiningDate(request.getJoiningDate())
                .employmentStatus(
                        EmploymentStatus.ACTIVE)
                .employmentType(
                        request.getEmploymentType())
                .departmentId(request.getDepartmentId())
                .departmentName(request.getDepartmentName())
                .designationId(request.getDesignationId())
                .designationName(request.getDesignationName())
                .managerId(request.getManagerId())
                .nationality(request.getNationality())
                .workLocation(request.getWorkLocation())
                .addresses(request.getAddresses())
                .isDeleted(false)
                .build();
    }

    public EmployeeResponse toResponse(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .middleName(employee.getMiddleName())
                .lastName(employee.getLastName())
                .gender(employee.getGender())
                .dateOfBirth(employee.getDateOfBirth())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .joiningDate(employee.getJoiningDate())
                .employmentStatus(
                        employee.getEmploymentStatus())
                .employmentType(
                        employee.getEmploymentType())
                .departmentId(employee.getDepartmentId())
                .departmentName(employee.getDepartmentName())
                .designationId(employee.getDesignationId())
                .designationName(employee.getDesignationName())
                .managerId(employee.getManagerId())
                .nationality(employee.getNationality())
                .workLocation(employee.getWorkLocation())
                .addresses(employee.getAddresses())
                .createdAt(employee.getCreatedAt())
                .build();
    }

    private String generateEmployeeCode() {
        return "EMP" + System.currentTimeMillis();
    }
    
    public void updateEntity(
            Employee employee,
            UpdateEmployeeRequest request) {

        employee.setFirstName(request.getFirstName());
        employee.setMiddleName(request.getMiddleName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setAlternatePhoneNumber(
                request.getAlternatePhoneNumber());
        employee.setEmploymentType(
                request.getEmploymentType());
        employee.setDepartmentId(
                request.getDepartmentId());
        employee.setDepartmentName(
                request.getDepartmentName());
        employee.setDesignationId(
                request.getDesignationId());
        employee.setDesignationName(
                request.getDesignationName());
        employee.setManagerId(request.getManagerId());
        employee.setNationality(request.getNationality());
        employee.setWorkLocation(
                request.getWorkLocation());
        employee.setAddresses(request.getAddresses());
    }
}