package com.ibm.payroll.service.impl;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.entity.EmployeeRating;
import com.ibm.payroll.entity.Goal;
import com.ibm.payroll.entity.PerformanceReview;
import com.ibm.payroll.payload.request.EmployeeRatingRequest;
import com.ibm.payroll.payload.request.GoalRequest;
import com.ibm.payroll.payload.request.PerformanceReviewRequest;
import com.ibm.payroll.payload.response.EmployeeRatingResponse;
import com.ibm.payroll.payload.response.GoalResponse;
import com.ibm.payroll.payload.response.KpiResponse;
import com.ibm.payroll.payload.response.PerformanceReviewResponse;
import com.ibm.payroll.repository.EmployeeRatingRepository;
import com.ibm.payroll.repository.GoalRepository;
import com.ibm.payroll.repository.KpiRepository;
import com.ibm.payroll.repository.PerformanceReviewRepository;
import com.ibm.payroll.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRatingRepository employeeRatingRepository;
    private final GoalRepository goalRepository;
    private final KpiRepository kpiRepository;

    @Override
    public ApiResponse<PerformanceReviewResponse> createReview(PerformanceReviewRequest request) {

        PerformanceReview review = PerformanceReview.builder()
                .employeeId(request.getEmployeeId())
                .reviewerId(request.getReviewerId())
                .period(request.getPeriod())
                .comments(request.getComments())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        PerformanceReview saved = performanceReviewRepository.save(review);

        return new ApiResponse<>(true, "Performance review created successfully", toResponse(saved));
    }

    @Override
    public ApiResponse<List<PerformanceReviewResponse>> getReviews(String employeeId) {

        List<PerformanceReviewResponse> reviews = performanceReviewRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(this::toResponse)
                .toList();

        return new ApiResponse<>(true, "Performance reviews fetched successfully", reviews);
    }

    @Override
    public ApiResponse<EmployeeRatingResponse> createRating(EmployeeRatingRequest request) {

        EmployeeRating rating = EmployeeRating.builder()
                .employeeId(request.getEmployeeId())
                .reviewId(request.getReviewId())
                .score(request.getScore())
                .category(request.getCategory())
                .ratedAt(LocalDateTime.now())
                .build();

        EmployeeRating saved = employeeRatingRepository.save(rating);

        EmployeeRatingResponse response = EmployeeRatingResponse.builder()
                .id(saved.getId())
                .employeeId(saved.getEmployeeId())
                .reviewId(saved.getReviewId())
                .score(saved.getScore())
                .category(saved.getCategory())
                .ratedAt(saved.getRatedAt())
                .build();

        return new ApiResponse<>(true, "Employee rating recorded successfully", response);
    }

    @Override
    public ApiResponse<GoalResponse> createGoal(GoalRequest request) {

        Goal goal = Goal.builder()
                .employeeId(request.getEmployeeId())
                .title(request.getTitle())
                .description(request.getDescription())
                .targetDate(request.getTargetDate())
                .status(request.getStatus())
                .build();

        Goal saved = goalRepository.save(goal);

        GoalResponse response = GoalResponse.builder()
                .id(saved.getId())
                .employeeId(saved.getEmployeeId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .targetDate(saved.getTargetDate())
                .status(saved.getStatus())
                .build();

        return new ApiResponse<>(true, "Goal created successfully", response);
    }

    @Override
    public ApiResponse<List<KpiResponse>> getKpis(String employeeId) {

        List<KpiResponse> kpis = kpiRepository.findByEmployeeId(employeeId)
                .stream()
                .map(kpi -> KpiResponse.builder()
                        .id(kpi.getId())
                        .employeeId(kpi.getEmployeeId())
                        .metricName(kpi.getMetricName())
                        .targetValue(kpi.getTargetValue())
                        .actualValue(kpi.getActualValue())
                        .period(kpi.getPeriod())
                        .build())
                .toList();

        return new ApiResponse<>(true, "KPIs fetched successfully", kpis);
    }

    private PerformanceReviewResponse toResponse(PerformanceReview review) {
        return PerformanceReviewResponse.builder()
                .id(review.getId())
                .employeeId(review.getEmployeeId())
                .reviewerId(review.getReviewerId())
                .period(review.getPeriod())
                .comments(review.getComments())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
