package com.ibm.payroll.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayslipResponse {

    private String id;

    private String employeeId;

    private String employeeName;

    private String designation;

    private String period;

    private Double earnings;

    private Double deductions;

    private Double netPay;

    private LocalDateTime generatedAt;
}
