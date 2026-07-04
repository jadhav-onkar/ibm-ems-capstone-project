package com.ibm.payroll.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryStructureRequest {

    @NotBlank(message = "Employee id is required")
    private String employeeId;

    @NotNull(message = "Basic salary is required")
    @PositiveOrZero(message = "Basic salary must not be negative")
    private Double basic;

    @NotNull(message = "HRA is required")
    @PositiveOrZero(message = "HRA must not be negative")
    private Double hra;

    @PositiveOrZero(message = "Allowances must not be negative")
    private Double allowances;

    @PositiveOrZero(message = "Deductions must not be negative")
    private Double deductions;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;
}
