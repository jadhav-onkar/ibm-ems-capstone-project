package com.ibm.payroll.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollRunResponse {

    private String id;

    private String period;

    private String status;

    private List<PayslipResponse> payslips;

    private LocalDateTime generatedAt;
}
