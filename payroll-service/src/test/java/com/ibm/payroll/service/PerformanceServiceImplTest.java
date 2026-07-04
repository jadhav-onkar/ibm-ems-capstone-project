package com.ibm.payroll.service;

import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.entity.Goal;
import com.ibm.payroll.entity.PerformanceReview;
import com.ibm.payroll.payload.request.GoalRequest;
import com.ibm.payroll.payload.request.PerformanceReviewRequest;
import com.ibm.payroll.payload.response.GoalResponse;
import com.ibm.payroll.payload.response.PerformanceReviewResponse;
import com.ibm.payroll.repository.EmployeeRatingRepository;
import com.ibm.payroll.repository.GoalRepository;
import com.ibm.payroll.repository.KpiRepository;
import com.ibm.payroll.repository.PerformanceReviewRepository;
import com.ibm.payroll.service.impl.PerformanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceImplTest {

    @Mock
    private PerformanceReviewRepository performanceReviewRepository;

    @Mock
    private EmployeeRatingRepository employeeRatingRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private KpiRepository kpiRepository;

    @InjectMocks
    private PerformanceServiceImpl performanceService;

    @Test
    void createReview_persistsAndReturnsReview() {
        PerformanceReviewRequest request = new PerformanceReviewRequest();
        request.setEmployeeId("EMP1");
        request.setReviewerId("MGR1");
        request.setPeriod("2026-Q2");
        request.setComments("Great work");
        request.setStatus("SUBMITTED");

        when(performanceReviewRepository.save(any(PerformanceReview.class)))
                .thenAnswer(invocation -> {
                    PerformanceReview review = invocation.getArgument(0);
                    review.setId("R1");
                    return review;
                });

        ApiResponse<PerformanceReviewResponse> response = performanceService.createReview(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getId()).isEqualTo("R1");
        assertThat(response.getData().getEmployeeId()).isEqualTo("EMP1");
    }

    @Test
    void getReviews_returnsEmptyListWhenNoneExist() {
        when(performanceReviewRepository.findByEmployeeId("EMP1")).thenReturn(List.of());

        ApiResponse<List<PerformanceReviewResponse>> response = performanceService.getReviews("EMP1");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEmpty();
    }

    @Test
    void createGoal_persistsAndReturnsGoal() {
        GoalRequest request = new GoalRequest();
        request.setEmployeeId("EMP1");
        request.setTitle("Improve test coverage");
        request.setDescription("Reach 80% coverage");
        request.setTargetDate(LocalDate.now().plusMonths(1));
        request.setStatus("IN_PROGRESS");

        when(goalRepository.save(any(Goal.class)))
                .thenAnswer(invocation -> {
                    Goal goal = invocation.getArgument(0);
                    goal.setId("G1");
                    return goal;
                });

        ApiResponse<GoalResponse> response = performanceService.createGoal(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getId()).isEqualTo("G1");
        assertThat(response.getData().getTitle()).isEqualTo("Improve test coverage");
    }
}
