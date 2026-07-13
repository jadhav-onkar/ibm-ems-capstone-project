package com.ibm.payroll.client;

import com.ibm.payroll.client.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${auth-service.base-url}")
    private String authServiceBaseUrl;

    /**
     * Login to auth service
     */
    public Optional<LoginResponseDto> login(LoginRequestDto request) {
        try {
            var response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/auth/login",
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponseDto<LoginResponseDto>>() {}
            );

            return Optional.ofNullable(response.getBody())
                    .map(ApiResponseDto::getData);
        } catch (RestClientException ex) {
            log.error("Failed to login via auth-service: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get current user details
     */
    public Optional<UserResponseDto> getCurrentUser(String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

            var response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/users/me",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ApiResponseDto<UserResponseDto>>() {}
            );

            return Optional.ofNullable(response.getBody())
                    .map(ApiResponseDto::getData);
        } catch (RestClientException ex) {
            log.error("Failed to get current user from auth-service: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get user by ID
     */
    public Optional<UserResponseDto> getUserById(String userId, String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

            var response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/users/" + userId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ApiResponseDto<UserResponseDto>>() {}
            );

            return Optional.ofNullable(response.getBody())
                    .map(ApiResponseDto::getData);
        } catch (RestClientException ex) {
            log.error("Failed to get user {} from auth-service: {}", userId, ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get user by employee ID
     */
    public Optional<UserResponseDto> getUserByEmployeeId(String employeeId, String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

            var response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/users/employee/" + employeeId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ApiResponseDto<UserResponseDto>>() {}
            );

            return Optional.ofNullable(response.getBody())
                    .map(ApiResponseDto::getData);
        } catch (RestClientException ex) {
            log.error("Failed to get user by employeeId {} from auth-service: {}", employeeId, ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get all users (Admin only)
     */
    public Optional<List<UserResponseDto>> getAllUsers(String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);

            var response = restTemplate.exchange(
                    authServiceBaseUrl + "/api/v1/users",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ApiResponseDto<List<UserResponseDto>>>() {}
            );

            return Optional.ofNullable(response.getBody())
                    .map(ApiResponseDto::getData);
        } catch (RestClientException ex) {
            log.error("Failed to get all users from auth-service: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
