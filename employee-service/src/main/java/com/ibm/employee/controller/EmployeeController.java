package com.ibm.employee.controller;

import java.util.List;

import com.ibm.employee.dto.request.CreateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateStatusRequest;
import com.ibm.employee.dto.response.EmployeeResponse;
import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ============================
    // CREATE EMPLOYEE
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid
            @RequestBody CreateEmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(request));
    }

    // ============================
    // GET EMPLOYEE BY ID
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }

    // ============================
    // UPDATE EMPLOYEE
    // ADMIN ONLY
    // ============================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String id,
            @RequestBody UpdateEmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request));
    }

    // ============================
    // DELETE EMPLOYEE
    // ADMIN ONLY
    // ============================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable String id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // ============================
    // GET EMPLOYEE BY CODE
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeResponse> getByCode(
            @PathVariable String employeeCode) {

        return ResponseEntity.ok(
                employeeService.getEmployeeByCode(employeeCode));
    }

    // ============================
    // GET EMPLOYEE BY DEPARTMENT
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(
            @PathVariable String departmentId) {

        return ResponseEntity.ok(
                employeeService.getByDepartment(departmentId));
    }

    // ============================
    // GET EMPLOYEE BY DESIGNATION
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/designation/{designationId}")
    public ResponseEntity<List<EmployeeResponse>> getByDesignation(
            @PathVariable String designationId) {

        return ResponseEntity.ok(
                employeeService.getByDesignation(designationId));
    }

    // ============================
    // GET EMPLOYEE BY MANAGER
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<EmployeeResponse>> getByManager(
            @PathVariable String managerId) {

        return ResponseEntity.ok(
                employeeService.getByManager(managerId));
    }

    // ============================
    // GET EMPLOYEE BY STATUS
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponse>> getByStatus(
            @PathVariable EmploymentStatus status) {

        return ResponseEntity.ok(
                employeeService.getByStatus(status));
    }

    // ============================
    // UPDATE STATUS
    // ADMIN ONLY
    // ============================

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> updateStatus(
            @PathVariable String id,
            @RequestBody UpdateStatusRequest request) {

        return ResponseEntity.ok(
                employeeService.updateStatus(id, request));
    }

    // ============================
    // PAGINATION
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "firstName") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    // ============================
    // GET ALL EMPLOYEES
    // ADMIN & MANAGER
    // ============================

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

//        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    // ============================
    // CURRENT USER
    // ANY AUTHENTICATED USER
    // ============================

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String currentUser(Authentication authentication) {

//        System.out.println(authentication);

        authentication.getAuthorities()
                .forEach(authority ->
                        System.out.println("Authority = " + authority.getAuthority()));

        return authentication.getAuthorities().toString();
    }
}