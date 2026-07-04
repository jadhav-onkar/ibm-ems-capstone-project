package com.ibm.payroll.service.impl;

import com.ibm.payroll.client.EmployeeClient;
import com.ibm.payroll.client.EmployeeDto;
import com.ibm.payroll.common.exception.PayslipNotFoundException;
import com.ibm.payroll.common.exception.SalaryStructureNotFoundException;
import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.entity.PayrollRun;
import com.ibm.payroll.entity.Payslip;
import com.ibm.payroll.entity.SalaryHistory;
import com.ibm.payroll.entity.SalaryStructure;
import com.ibm.payroll.payload.request.GeneratePayrollRequest;
import com.ibm.payroll.payload.response.PayrollRunResponse;
import com.ibm.payroll.payload.response.PayslipResponse;
import com.ibm.payroll.payload.response.SalaryHistoryResponse;
import com.ibm.payroll.repository.PayrollRunRepository;
import com.ibm.payroll.repository.PayslipRepository;
import com.ibm.payroll.repository.SalaryHistoryRepository;
import com.ibm.payroll.repository.SalaryStructureRepository;
import com.ibm.payroll.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final SalaryStructureRepository salaryStructureRepository;
    private final PayrollRunRepository payrollRunRepository;
    private final PayslipRepository payslipRepository;
    private final SalaryHistoryRepository salaryHistoryRepository;
    private final EmployeeClient employeeClient;

    @Override
    public ApiResponse<PayrollRunResponse> generatePayroll(GeneratePayrollRequest request, String authorizationHeader) {

        List<PayslipResponse> payslipResponses = request.getEmployeeIds().stream()
                .map(employeeId -> generatePayslipForEmployee(employeeId, request.getPeriod(), authorizationHeader))
                .toList();

        PayrollRun run = PayrollRun.builder()
                .period(request.getPeriod())
                .status("COMPLETED")
                .employeeIds(request.getEmployeeIds())
                .generatedAt(LocalDateTime.now())
                .build();

        PayrollRun savedRun = payrollRunRepository.save(run);

        PayrollRunResponse response = PayrollRunResponse.builder()
                .id(savedRun.getId())
                .period(savedRun.getPeriod())
                .status(savedRun.getStatus())
                .payslips(payslipResponses)
                .generatedAt(savedRun.getGeneratedAt())
                .build();

        return new ApiResponse<>(true, "Payroll generated successfully", response);
    }

    private PayslipResponse generatePayslipForEmployee(String employeeId, String period, String authorizationHeader) {

        SalaryStructure structure = salaryStructureRepository
                .findTopByEmployeeIdOrderByEffectiveDateDesc(employeeId)
                .orElseThrow(() -> new SalaryStructureNotFoundException(
                        "Salary structure not found for employee " + employeeId));

        double earnings = structure.getBasic() + structure.getHra()
                + (structure.getAllowances() == null ? 0 : structure.getAllowances());
        double deductions = structure.getDeductions() == null ? 0 : structure.getDeductions();
        double netPay = earnings - deductions;

        Optional<EmployeeDto> employee = employeeClient.getEmployee(employeeId, authorizationHeader);

        Payslip payslip = Payslip.builder()
                .employeeId(employeeId)
                .employeeName(employee.map(EmployeeDto::getName).orElse(null))
                .designation(employee.map(EmployeeDto::getDesignation).orElse(null))
                .period(period)
                .earnings(earnings)
                .deductions(deductions)
                .netPay(netPay)
                .generatedAt(LocalDateTime.now())
                .build();

        Payslip savedPayslip = payslipRepository.save(payslip);

        salaryHistoryRepository.save(SalaryHistory.builder()
                .employeeId(employeeId)
                .period(period)
                .netPay(netPay)
                .recordedAt(LocalDateTime.now())
                .build());

        return toResponse(savedPayslip);
    }

    @Override
    public ApiResponse<PayslipResponse> getPayslip(String employeeId, String period) {

        Payslip payslip = payslipRepository.findByEmployeeIdAndPeriod(employeeId, period)
                .orElseThrow(() -> new PayslipNotFoundException(
                        "Payslip not found for employee " + employeeId + " and period " + period));

        return new ApiResponse<>(true, "Payslip fetched successfully", toResponse(payslip));
    }

    @Override
    public ApiResponse<List<SalaryHistoryResponse>> getHistory(String employeeId) {

        List<SalaryHistoryResponse> history = salaryHistoryRepository
                .findByEmployeeIdOrderByRecordedAtDesc(employeeId)
                .stream()
                .map(entry -> SalaryHistoryResponse.builder()
                        .id(entry.getId())
                        .employeeId(entry.getEmployeeId())
                        .period(entry.getPeriod())
                        .netPay(entry.getNetPay())
                        .recordedAt(entry.getRecordedAt())
                        .build())
                .toList();

        return new ApiResponse<>(true, "Salary history fetched successfully", history);
    }

    private PayslipResponse toResponse(Payslip payslip) {
        return PayslipResponse.builder()
                .id(payslip.getId())
                .employeeId(payslip.getEmployeeId())
                .employeeName(payslip.getEmployeeName())
                .designation(payslip.getDesignation())
                .period(payslip.getPeriod())
                .earnings(payslip.getEarnings())
                .deductions(payslip.getDeductions())
                .netPay(payslip.getNetPay())
                .generatedAt(payslip.getGeneratedAt())
                .build();
    }
}
