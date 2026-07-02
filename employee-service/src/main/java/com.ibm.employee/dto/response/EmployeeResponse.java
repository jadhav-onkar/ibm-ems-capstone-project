package com.ibm.employee.dto.response;

import com.ibm.employee.entity.Address;
import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.entity.enums.EmploymentType;
import com.ibm.employee.entity.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class EmployeeResponse {

    private String id;

    private String employeeCode;

    private String firstName;

    private String middleName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String email;

    private String phoneNumber;

    private LocalDate joiningDate;

    private EmploymentStatus employmentStatus;

    private EmploymentType employmentType;

    private String departmentId;

    private String departmentName;

    private String designationId;

    private String designationName;

    private String managerId;

    private String nationality;

    private String workLocation;

    private List<Address> addresses;

    private Instant createdAt;
}