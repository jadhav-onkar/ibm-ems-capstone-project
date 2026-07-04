package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payslips")
public class Payslip {

    @Id
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
