package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payroll_runs")
public class PayrollRun {

    @Id
    private String id;

    private String period;

    private String status;

    private List<String> employeeIds;

    private LocalDateTime generatedAt;
}
