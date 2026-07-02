package com.ibm.employee.dto.request;

import com.ibm.employee.entity.Address;
import com.ibm.employee.entity.enums.EmploymentType;
import com.ibm.employee.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateEmployeeRequest {

    private String firstName;

    private String middleName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String alternatePhoneNumber;

    private EmploymentType employmentType;

    private String departmentId;

    private String departmentName;

    private String designationId;

    private String designationName;

    private String managerId;

    private String nationality;

    private String workLocation;

    private List<Address> addresses;
}