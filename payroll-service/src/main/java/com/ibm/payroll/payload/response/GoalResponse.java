package com.ibm.payroll.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponse {

    private String id;

    private String employeeId;

    private String title;

    private String description;

    private LocalDate targetDate;

    private String status;
}
