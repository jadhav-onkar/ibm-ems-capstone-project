package com.ibm.payroll.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoalRequest {

    @NotBlank(message = "Employee id is required")
    private String employeeId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Target date is required")
    private LocalDate targetDate;

    @NotBlank(message = "Status is required")
    private String status;
}
