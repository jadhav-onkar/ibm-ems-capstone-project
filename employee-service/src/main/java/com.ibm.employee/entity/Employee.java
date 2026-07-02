package com.ibm.employee.entity;

import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.entity.enums.EmploymentType;
import com.ibm.employee.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    @Indexed(unique = true)
    private String employeeCode;

    private String firstName;

    private String middleName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String alternatePhoneNumber;

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

    private boolean isDeleted;

    private List<Address> addresses;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}