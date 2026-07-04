package com.ibm.payroll.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerformanceReviewRequest {

    @NotBlank(message = "Employee id is required")
    private String employeeId;

    @NotBlank(message = "Reviewer id is required")
    private String reviewerId;

    @NotBlank(message = "Period is required")
    private String period;

    private String comments;

    @NotBlank(message = "Status is required")
    private String status;
}
