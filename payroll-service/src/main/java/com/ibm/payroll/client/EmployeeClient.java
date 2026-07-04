package com.ibm.payroll.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeClient {

    private final RestTemplate restTemplate;

    @Value("${employee-service.base-url}")
    private String employeeServiceBaseUrl;

    public Optional<EmployeeDto> getEmployee(String employeeId, String authorizationHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (authorizationHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }

            var response = restTemplate.exchange(
                    employeeServiceBaseUrl + "/api/employees/" + employeeId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    EmployeeDto.class
            );

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException ex) {
            log.warn("Failed to fetch employee {} from employee-service: {}", employeeId, ex.getMessage());
            return Optional.empty();
        }
    }
}
