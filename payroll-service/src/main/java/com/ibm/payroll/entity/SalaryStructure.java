package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "salary_structures")
public class SalaryStructure {

    @Id
    private String id;

    private String employeeId;

    private Double basic;

    private Double hra;

    private Double allowances;

    private Double deductions;

    private LocalDate effectiveDate;
}
