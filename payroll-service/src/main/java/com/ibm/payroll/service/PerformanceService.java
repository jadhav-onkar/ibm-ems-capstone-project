package com.ibm.payroll.service;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.payload.request.EmployeeRatingRequest;
import com.ibm.payroll.payload.request.GoalRequest;
import com.ibm.payroll.payload.request.PerformanceReviewRequest;
import com.ibm.payroll.payload.response.EmployeeRatingResponse;
import com.ibm.payroll.payload.response.GoalResponse;
import com.ibm.payroll.payload.response.KpiResponse;
import com.ibm.payroll.payload.response.PerformanceReviewResponse;

import java.util.List;

public interface PerformanceService {

    ApiResponse<PerformanceReviewResponse> createReview(PerformanceReviewRequest request);

    ApiResponse<List<PerformanceReviewResponse>> getReviews(String employeeId);

    ApiResponse<EmployeeRatingResponse> createRating(EmployeeRatingRequest request);

    ApiResponse<GoalResponse> createGoal(GoalRequest request);

    ApiResponse<List<KpiResponse>> getKpis(String employeeId);
}
