package com.ibm.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.payroll.common.exception.PayslipNotFoundException;
import com.ibm.payroll.common.exception.SalaryStructureNotFoundException;
import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.common.security.JwtUtil;
import com.ibm.payroll.payload.request.GeneratePayrollRequest;
import com.ibm.payroll.payload.request.SalaryStructureRequest;
import com.ibm.payroll.payload.response.PayrollRunResponse;
import com.ibm.payroll.payload.response.PayslipResponse;
import com.ibm.payroll.payload.response.SalaryHistoryResponse;
import com.ibm.payroll.payload.response.SalaryStructureResponse;
import com.ibm.payroll.service.PayrollService;
import com.ibm.payroll.service.SalaryStructureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PayrollController.class)
@WithMockUser
class PayrollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SalaryStructureService salaryStructureService;

    @MockBean
    private PayrollService payrollService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void defineSalaryStructure_returnsOk() throws Exception {
        SalaryStructureRequest request = new SalaryStructureRequest();
        request.setEmployeeId("EMP1");
        request.setBasic(30000.0);
        request.setHra(10000.0);
        request.setAllowances(2000.0);
        request.setDeductions(1000.0);
        request.setEffectiveDate(LocalDate.now());

        SalaryStructureResponse response = SalaryStructureResponse.builder()
                .id("S1").employeeId("EMP1").basic(30000.0).hra(10000.0)
                .allowances(2000.0).deductions(1000.0).effectiveDate(LocalDate.now())
                .build();

        when(salaryStructureService.defineOrUpdate(any()))
                .thenReturn(new ApiResponse<>(true, "Salary structure saved successfully", response));

        mockMvc.perform(post("/api/payroll/salary-structure")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.employeeId").value("EMP1"));
    }

    @Test
    void defineSalaryStructure_missingEmployeeId_returnsBadRequest() throws Exception {
        SalaryStructureRequest request = new SalaryStructureRequest();
        request.setBasic(30000.0);
        request.setHra(10000.0);
        request.setEffectiveDate(LocalDate.now());

        mockMvc.perform(post("/api/payroll/salary-structure")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSalaryStructure_notFound_returns404() throws Exception {
        when(salaryStructureService.getByEmployeeId("EMP404"))
                .thenThrow(new SalaryStructureNotFoundException("Salary structure not found for employee EMP404"));

        mockMvc.perform(get("/api/payroll/salary-structure/EMP404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void generatePayroll_returnsOk() throws Exception {
        GeneratePayrollRequest request = new GeneratePayrollRequest();
        request.setPeriod("2026-07");
        request.setEmployeeIds(List.of("EMP1"));

        PayrollRunResponse response = PayrollRunResponse.builder()
                .id("R1").period("2026-07").status("COMPLETED")
                .payslips(List.of())
                .generatedAt(LocalDateTime.now())
                .build();

        when(payrollService.generatePayroll(any(), anyString()))
                .thenReturn(new ApiResponse<>(true, "Payroll generated successfully", response));

        mockMvc.perform(post("/api/payroll/generate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    void getPayslip_notFound_returns404() throws Exception {
        when(payrollService.getPayslip("EMP1", "2026-07"))
                .thenThrow(new PayslipNotFoundException("Payslip not found for employee EMP1 and period 2026-07"));

        mockMvc.perform(get("/api/payroll/payslip/EMP1/2026-07"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHistory_returnsOk() throws Exception {
        when(payrollService.getHistory("EMP1"))
                .thenReturn(new ApiResponse<>(true, "Salary history fetched successfully",
                        List.of(SalaryHistoryResponse.builder()
                                .id("H1").employeeId("EMP1").period("2026-07").netPay(39000.0)
                                .recordedAt(LocalDateTime.now()).build())));

        mockMvc.perform(get("/api/payroll/history/EMP1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].netPay").value(39000.0));
    }
}
