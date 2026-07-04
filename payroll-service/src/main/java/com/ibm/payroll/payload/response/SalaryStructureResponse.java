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
public class SalaryStructureResponse {

    private String id;

    private String employeeId;

    private Double basic;

    private Double hra;

    private Double allowances;

    private Double deductions;

    private LocalDate effectiveDate;
}
