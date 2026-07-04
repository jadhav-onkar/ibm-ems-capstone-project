package com.ibm.payroll.service.impl;

import com.ibm.payroll.common.exception.SalaryStructureNotFoundException;
import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.entity.SalaryStructure;
import com.ibm.payroll.payload.request.SalaryStructureRequest;
import com.ibm.payroll.payload.response.SalaryStructureResponse;
import com.ibm.payroll.repository.SalaryStructureRepository;
import com.ibm.payroll.service.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryStructureServiceImpl implements SalaryStructureService {

    private final SalaryStructureRepository salaryStructureRepository;

    @Override
    public ApiResponse<SalaryStructureResponse> defineOrUpdate(SalaryStructureRequest request) {

        SalaryStructure structure = salaryStructureRepository
                .findTopByEmployeeIdOrderByEffectiveDateDesc(request.getEmployeeId())
                .orElse(new SalaryStructure());

        structure.setEmployeeId(request.getEmployeeId());
        structure.setBasic(request.getBasic());
        structure.setHra(request.getHra());
        structure.setAllowances(request.getAllowances());
        structure.setDeductions(request.getDeductions());
        structure.setEffectiveDate(request.getEffectiveDate());

        SalaryStructure saved = salaryStructureRepository.save(structure);

        return new ApiResponse<>(true, "Salary structure saved successfully", toResponse(saved));
    }

    @Override
    public ApiResponse<SalaryStructureResponse> getByEmployeeId(String employeeId) {

        SalaryStructure structure = salaryStructureRepository
                .findTopByEmployeeIdOrderByEffectiveDateDesc(employeeId)
                .orElseThrow(() -> new SalaryStructureNotFoundException(
                        "Salary structure not found for employee " + employeeId));

        return new ApiResponse<>(true, "Salary structure fetched successfully", toResponse(structure));
    }

    private SalaryStructureResponse toResponse(SalaryStructure structure) {
        return SalaryStructureResponse.builder()
                .id(structure.getId())
                .employeeId(structure.getEmployeeId())
                .basic(structure.getBasic())
                .hra(structure.getHra())
                .allowances(structure.getAllowances())
                .deductions(structure.getDeductions())
                .effectiveDate(structure.getEffectiveDate())
                .build();
    }
}
