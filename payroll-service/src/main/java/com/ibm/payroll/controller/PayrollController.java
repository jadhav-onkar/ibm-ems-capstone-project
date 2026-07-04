package com.ibm.payroll.controller;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.payload.request.GeneratePayrollRequest;
import com.ibm.payroll.payload.request.SalaryStructureRequest;
import com.ibm.payroll.payload.response.PayrollRunResponse;
import com.ibm.payroll.payload.response.PayslipResponse;
import com.ibm.payroll.payload.response.SalaryHistoryResponse;
import com.ibm.payroll.payload.response.SalaryStructureResponse;
import com.ibm.payroll.service.PayrollService;
import com.ibm.payroll.service.SalaryStructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll Management", description = "Salary structure, payroll generation, payslips and salary history")
public class PayrollController {

    private final SalaryStructureService salaryStructureService;
    private final PayrollService payrollService;

    @PostMapping("/salary-structure")
    @Operation(summary = "Define or update salary structure for an employee")
    public ResponseEntity<ApiResponse<SalaryStructureResponse>> defineSalaryStructure(
            @Valid @RequestBody SalaryStructureRequest request) {
        return ResponseEntity.ok(salaryStructureService.defineOrUpdate(request));
    }

    @GetMapping("/salary-structure/{employeeId}")
    @Operation(summary = "Get the current salary structure for an employee")
    public ResponseEntity<ApiResponse<SalaryStructureResponse>> getSalaryStructure(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(salaryStructureService.getByEmployeeId(employeeId));
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate payroll for a period across a set of employees")
    public ResponseEntity<ApiResponse<PayrollRunResponse>> generatePayroll(
            @Valid @RequestBody GeneratePayrollRequest request,
            HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(payrollService.generatePayroll(request, authorizationHeader));
    }

    @GetMapping("/payslip/{employeeId}/{period}")
    @Operation(summary = "Get the payslip for an employee for a given period")
    public ResponseEntity<ApiResponse<PayslipResponse>> getPayslip(
            @PathVariable String employeeId,
            @PathVariable String period) {
        return ResponseEntity.ok(payrollService.getPayslip(employeeId, period));
    }

    @GetMapping("/history/{employeeId}")
    @Operation(summary = "Get the salary history for an employee")
    public ResponseEntity<ApiResponse<List<SalaryHistoryResponse>>> getHistory(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(payrollService.getHistory(employeeId));
    }
}
