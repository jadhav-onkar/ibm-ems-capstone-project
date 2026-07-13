package com.ibm.payroll.controller;

import com.ibm.payroll.client.AuthClient;
import com.ibm.payroll.client.dto.LoginRequestDto;
import com.ibm.payroll.client.dto.LoginResponseDto;
import com.ibm.payroll.client.dto.UserResponseDto;
import com.ibm.payroll.common.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Demo controller to showcase AuthClient usage in Payroll Service
 * This demonstrates how to consume auth-service endpoints
 */
@RestController
@RequestMapping("/api/v1/auth-demo")
@RequiredArgsConstructor
public class AuthDemoController {

    private final AuthClient authClient;

    /**
     * Demo endpoint: Login via auth-service
     * POST /api/v1/auth-demo/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {
        return authClient.login(request)
                .map(loginResponse -> ResponseEntity.ok(
                        new ApiResponse<>(true, "Login successful via auth-service", loginResponse)))
                .orElse(ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Login failed", null)));
    }

    /**
     * Demo endpoint: Get current user from auth-service
     * GET /api/v1/auth-demo/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        
        return authClient.getCurrentUser(authHeader)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(true, "User fetched from auth-service", user)))
                .orElse(ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Failed to fetch user", null)));
    }

    /**
     * Demo endpoint: Get user by ID from auth-service
     * GET /api/v1/auth-demo/users/{userId}
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable String userId,
            @RequestHeader("Authorization") String authHeader) {
        
        return authClient.getUserById(userId, authHeader)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(true, "User fetched from auth-service", user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Demo endpoint: Get user by employee ID from auth-service
     * GET /api/v1/auth-demo/employees/{employeeId}/user
     */
    @GetMapping("/employees/{employeeId}/user")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmployeeId(
            @PathVariable String employeeId,
            @RequestHeader("Authorization") String authHeader) {
        
        return authClient.getUserByEmployeeId(employeeId, authHeader)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(true, "User fetched from auth-service by employeeId", user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Demo endpoint: Get all users from auth-service (Admin only)
     * GET /api/v1/auth-demo/users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers(
            @RequestHeader("Authorization") String authHeader) {
        
        return authClient.getAllUsers(authHeader)
                .map(users -> ResponseEntity.ok(
                        new ApiResponse<>(true, "All users fetched from auth-service", users)))
                .orElse(ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Failed to fetch users", null)));
    }
}
