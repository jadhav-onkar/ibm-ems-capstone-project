package com.ibm.employee.dto.request;

import com.ibm.employee.entity.Address;
import com.ibm.employee.entity.enums.EmploymentType;
import com.ibm.employee.entity.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Gender is mandatory")
    private Gender gender;

    private LocalDate dateOfBirth;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number")
    private String phoneNumber;

    private String alternatePhoneNumber;

    private LocalDate joiningDate;

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