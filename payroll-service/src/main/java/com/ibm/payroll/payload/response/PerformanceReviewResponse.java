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
public class PerformanceReviewResponse {

    private String id;

    private String employeeId;

    private String reviewerId;

    private String period;

    private String comments;

    private String status;

    private LocalDateTime createdAt;
}
