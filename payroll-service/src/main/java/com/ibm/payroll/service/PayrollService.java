package com.ibm.payroll.service;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.payload.request.GeneratePayrollRequest;
import com.ibm.payroll.payload.response.PayrollRunResponse;
import com.ibm.payroll.payload.response.PayslipResponse;
import com.ibm.payroll.payload.response.SalaryHistoryResponse;

import java.util.List;

public interface PayrollService {

    ApiResponse<PayrollRunResponse> generatePayroll(GeneratePayrollRequest request, String authorizationHeader);

    ApiResponse<PayslipResponse> getPayslip(String employeeId, String period);

    ApiResponse<List<SalaryHistoryResponse>> getHistory(String employeeId);
}
