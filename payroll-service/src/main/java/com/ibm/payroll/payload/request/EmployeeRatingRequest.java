package com.ibm.payroll.payload.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRatingRequest {

    @NotBlank(message = "Employee id is required")
    private String employeeId;

    @NotBlank(message = "Review id is required")
    private String reviewId;

    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.0", message = "Score must be at least 0")
    @DecimalMax(value = "5.0", message = "Score must be at most 5")
    private Double score;

    @NotBlank(message = "Category is required")
    private String category;
}
