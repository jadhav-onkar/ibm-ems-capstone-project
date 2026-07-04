package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "kpis")
public class KPI {

    @Id
    private String id;

    private String employeeId;

    private String metricName;

    private Double targetValue;

    private Double actualValue;

    private String period;
}
