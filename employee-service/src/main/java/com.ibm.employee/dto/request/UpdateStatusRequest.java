package com.ibm.employee.dto.request;

import com.ibm.employee.entity.enums.EmploymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {

    @NotNull(message = "Status is mandatory")
    private EmploymentStatus status;
}