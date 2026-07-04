package com.ibm.payroll.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class GeneratePayrollRequest {

    @NotBlank(message = "Period is required")
    private String period;

    @NotEmpty(message = "At least one employee id is required")
    private List<String> employeeIds;
}
