package com.ibm.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.payroll.common.payload.ApiResponse;
import com.ibm.payroll.common.security.JwtUtil;
import com.ibm.payroll.payload.request.EmployeeRatingRequest;
import com.ibm.payroll.payload.request.GoalRequest;
import com.ibm.payroll.payload.request.PerformanceReviewRequest;
import com.ibm.payroll.payload.response.EmployeeRatingResponse;
import com.ibm.payroll.payload.response.GoalResponse;
import com.ibm.payroll.payload.response.KpiResponse;
import com.ibm.payroll.payload.response.PerformanceReviewResponse;
import com.ibm.payroll.service.PerformanceService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerformanceController.class)
@WithMockUser
class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerformanceService performanceService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void createReview_returnsOk() throws Exception {
        PerformanceReviewRequest request = new PerformanceReviewRequest();
        request.setEmployeeId("EMP1");
        request.setReviewerId("MGR1");
        request.setPeriod("2026-Q2");
        request.setStatus("SUBMITTED");

        PerformanceReviewResponse response = PerformanceReviewResponse.builder()
                .id("R1").employeeId("EMP1").reviewerId("MGR1").period("2026-Q2")
                .status("SUBMITTED").createdAt(LocalDateTime.now())
                .build();

        when(performanceService.createReview(any()))
                .thenReturn(new ApiResponse<>(true, "Performance review created successfully", response));

        mockMvc.perform(post("/api/performance/review")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employeeId").value("EMP1"));
    }

    @Test
    void createReview_missingStatus_returnsBadRequest() throws Exception {
        PerformanceReviewRequest request = new PerformanceReviewRequest();
        request.setEmployeeId("EMP1");
        request.setReviewerId("MGR1");
        request.setPeriod("2026-Q2");

        mockMvc.perform(post("/api/performance/review")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviews_returnsOk() throws Exception {
        when(performanceService.getReviews("EMP1"))
                .thenReturn(new ApiResponse<>(true, "Performance reviews fetched successfully", List.of()));

        mockMvc.perform(get("/api/performance/review/EMP1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void createRating_returnsOk() throws Exception {
        EmployeeRatingRequest request = new EmployeeRatingRequest();
        request.setEmployeeId("EMP1");
        request.setReviewId("R1");
        request.setScore(4.5);
        request.setCategory("Technical");

        EmployeeRatingResponse response = EmployeeRatingResponse.builder()
                .id("RT1").employeeId("EMP1").reviewId("R1").score(4.5)
                .category("Technical").ratedAt(LocalDateTime.now())
                .build();

        when(performanceService.createRating(any()))
                .thenReturn(new ApiResponse<>(true, "Employee rating recorded successfully", response));

        mockMvc.perform(post("/api/performance/rating")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.score").value(4.5));
    }

    @Test
    void createGoal_returnsOk() throws Exception {
        GoalRequest request = new GoalRequest();
        request.setEmployeeId("EMP1");
        request.setTitle("Improve test coverage");
        request.setTargetDate(LocalDate.now().plusMonths(1));
        request.setStatus("IN_PROGRESS");

        GoalResponse response = GoalResponse.builder()
                .id("G1").employeeId("EMP1").title("Improve test coverage")
                .targetDate(request.getTargetDate()).status("IN_PROGRESS")
                .build();

        when(performanceService.createGoal(any()))
                .thenReturn(new ApiResponse<>(true, "Goal created successfully", response));

        mockMvc.perform(post("/api/performance/goals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Improve test coverage"));
    }

    @Test
    void getKpis_returnsOk() throws Exception {
        when(performanceService.getKpis("EMP1"))
                .thenReturn(new ApiResponse<>(true, "KPIs fetched successfully",
                        List.of(KpiResponse.builder()
                                .id("K1").employeeId("EMP1").metricName("Velocity")
                                .targetValue(20.0).actualValue(18.0).period("2026-Q2")
                                .build())));

        mockMvc.perform(get("/api/performance/kpi/EMP1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].metricName").value("Velocity"));
    }
}
