package com.ibm.payroll.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiResponse {

    private String id;

    private String employeeId;

    private String metricName;

    private Double targetValue;

    private Double actualValue;

    private String period;
}
