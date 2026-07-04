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
@Document(collection = "salary_history")
public class SalaryHistory {

    @Id
    private String id;

    private String employeeId;

    private String period;

    private Double netPay;

    private LocalDateTime recordedAt;
}
