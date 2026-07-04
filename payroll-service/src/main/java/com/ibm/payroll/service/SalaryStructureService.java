package com.ibm.payroll.service;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.payload.request.SalaryStructureRequest;
import com.ibm.payroll.payload.response.SalaryStructureResponse;

public interface SalaryStructureService {

    ApiResponse<SalaryStructureResponse> defineOrUpdate(SalaryStructureRequest request);

    ApiResponse<SalaryStructureResponse> getByEmployeeId(String employeeId);
}
