package com.ibm.payroll.controller;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.payload.request.EmployeeRatingRequest;
import com.ibm.payroll.payload.request.GoalRequest;
import com.ibm.payroll.payload.request.PerformanceReviewRequest;
import com.ibm.payroll.payload.response.EmployeeRatingResponse;
import com.ibm.payroll.payload.response.GoalResponse;
import com.ibm.payroll.payload.response.KpiResponse;
import com.ibm.payroll.payload.response.PerformanceReviewResponse;
import com.ibm.payroll.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
@Tag(name = "Performance Management", description = "Performance reviews, ratings, goals and KPI tracking")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping("/review")
    @Operation(summary = "Create a performance review for an employee")
    public ResponseEntity<ApiResponse<PerformanceReviewResponse>> createReview(
            @Valid @RequestBody PerformanceReviewRequest request) {
        return ResponseEntity.ok(performanceService.createReview(request));
    }

    @GetMapping("/review/{employeeId}")
    @Operation(summary = "Get all performance reviews for an employee")
    public ResponseEntity<ApiResponse<List<PerformanceReviewResponse>>> getReviews(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(performanceService.getReviews(employeeId));
    }

    @PostMapping("/rating")
    @Operation(summary = "Record an employee rating")
    public ResponseEntity<ApiResponse<EmployeeRatingResponse>> createRating(
            @Valid @RequestBody EmployeeRatingRequest request) {
        return ResponseEntity.ok(performanceService.createRating(request));
    }

    @PostMapping("/goals")
    @Operation(summary = "Set a goal for an employee")
    public ResponseEntity<ApiResponse<GoalResponse>> createGoal(
            @Valid @RequestBody GoalRequest request) {
        return ResponseEntity.ok(performanceService.createGoal(request));
    }

    @GetMapping("/kpi/{employeeId}")
    @Operation(summary = "Get KPI tracking data for an employee")
    public ResponseEntity<ApiResponse<List<KpiResponse>>> getKpis(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(performanceService.getKpis(employeeId));
    }
}
