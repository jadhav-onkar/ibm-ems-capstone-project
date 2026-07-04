package com.ibm.payroll.service;

import com.ibm.payroll.client.EmployeeClient;
import com.ibm.payroll.client.EmployeeDto;
import com.ibm.payroll.common.exception.PayslipNotFoundException;
import com.ibm.payroll.common.exception.SalaryStructureNotFoundException;
import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.entity.PayrollRun;
import com.ibm.payroll.entity.Payslip;
import com.ibm.payroll.entity.SalaryStructure;
import com.ibm.payroll.payload.request.GeneratePayrollRequest;
import com.ibm.payroll.payload.response.PayrollRunResponse;
import com.ibm.payroll.repository.PayrollRunRepository;
import com.ibm.payroll.repository.PayslipRepository;
import com.ibm.payroll.repository.SalaryHistoryRepository;
import com.ibm.payroll.repository.SalaryStructureRepository;
import com.ibm.payroll.service.impl.PayrollServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayrollServiceImplTest {

    @Mock
    private SalaryStructureRepository salaryStructureRepository;

    @Mock
    private PayrollRunRepository payrollRunRepository;

    @Mock
    private PayslipRepository payslipRepository;

    @Mock
    private SalaryHistoryRepository salaryHistoryRepository;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    @Test
    void generatePayroll_computesNetPayAndPersistsPayslip() {
        SalaryStructure structure = SalaryStructure.builder()
                .employeeId("EMP1")
                .basic(30000.0)
                .hra(10000.0)
                .allowances(2000.0)
                .deductions(3000.0)
                .effectiveDate(LocalDate.now())
                .build();

        when(salaryStructureRepository.findTopByEmployeeIdOrderByEffectiveDateDesc("EMP1"))
                .thenReturn(Optional.of(structure));
        when(employeeClient.getEmployee(anyString(), any()))
                .thenReturn(Optional.of(new EmployeeDto("EMP1", "Jane Doe", "Engineer")));
        when(payslipRepository.save(any(Payslip.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(payrollRunRepository.save(any(PayrollRun.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GeneratePayrollRequest request = new GeneratePayrollRequest();
        request.setPeriod("2026-07");
        request.setEmployeeIds(List.of("EMP1"));

        ApiResponse<PayrollRunResponse> response = payrollService.generatePayroll(request, "Bearer token");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getPayslips()).hasSize(1);
        assertThat(response.getData().getPayslips().get(0).getNetPay()).isEqualTo(39000.0);
        assertThat(response.getData().getPayslips().get(0).getEmployeeName()).isEqualTo("Jane Doe");
    }

    @Test
    void generatePayroll_throwsWhenSalaryStructureMissing() {
        when(salaryStructureRepository.findTopByEmployeeIdOrderByEffectiveDateDesc("EMP2"))
                .thenReturn(Optional.empty());

        GeneratePayrollRequest request = new GeneratePayrollRequest();
        request.setPeriod("2026-07");
        request.setEmployeeIds(List.of("EMP2"));

        assertThatThrownBy(() -> payrollService.generatePayroll(request, null))
                .isInstanceOf(SalaryStructureNotFoundException.class);
    }

    @Test
    void getPayslip_throwsWhenNotFound() {
        when(payslipRepository.findByEmployeeIdAndPeriod("EMP1", "2026-07"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> payrollService.getPayslip("EMP1", "2026-07"))
                .isInstanceOf(PayslipNotFoundException.class);
    }
}
