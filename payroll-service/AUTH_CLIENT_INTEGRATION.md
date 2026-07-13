# Auth Service Integration in Payroll Service

## Overview
This document describes the integration of auth-service endpoints into the payroll-service, allowing the payroll service to consume authentication and user management APIs from the auth-service.

## Changes Made

### 1. Configuration Changes

**File:** `src/main/resources/application.properties`

Added auth-service base URL configuration:
```properties
auth-service.base-url=http://localhost:8081
```

### 2. New DTOs Created

**Location:** `src/main/java/com/ibm/payroll/client/dto/`

#### a) `LoginRequestDto.java`
- Used for login requests to auth-service
- Fields: `username`, `password`

#### b) `LoginResponseDto.java`
- Response from auth-service login endpoint
- Fields: `token`, `username`, `roles`

#### c) `UserResponseDto.java`
- User details from auth-service
- Fields: `id`, `username`, `email`, `employeeId`, `roles`, `enabled`, `createdAt`, `updatedAt`

#### d) `ApiResponseDto.java`
- Generic wrapper for auth-service API responses
- Fields: `success`, `message`, `data`

### 3. AuthClient Implementation

**File:** `src/main/java/com/ibm/payroll/client/AuthClient.java`

A RestTemplate-based client that provides methods to interact with auth-service endpoints:

#### Available Methods:

1. **`login(LoginRequestDto request)`**
   - Authenticates user via auth-service
   - Returns JWT token and user details
   - Endpoint: `POST /api/v1/auth/login`

2. **`getCurrentUser(String authorizationHeader)`**
   - Gets current authenticated user details
   - Requires: Bearer token in Authorization header
   - Endpoint: `GET /api/v1/users/me`

3. **`getUserById(String userId, String authorizationHeader)`**
   - Gets user details by user ID
   - Requires: Bearer token in Authorization header
   - Endpoint: `GET /api/v1/users/{userId}`

4. **`getUserByEmployeeId(String employeeId, String authorizationHeader)`**
   - Gets user details by employee ID
   - Requires: Bearer token in Authorization header
   - Endpoint: `GET /api/v1/users/employee/{employeeId}`

5. **`getAllUsers(String authorizationHeader)`**
   - Gets all users (Admin only)
   - Requires: Bearer token in Authorization header
   - Endpoint: `GET /api/v1/users`

### 4. Demo Controller

**File:** `src/main/java/com/ibm/payroll/controller/AuthDemoController.java`

A demonstration controller showcasing how to use the AuthClient. Provides endpoints:

- `POST /api/v1/auth-demo/login` - Login via auth-service
- `GET /api/v1/auth-demo/me` - Get current user
- `GET /api/v1/auth-demo/users/{userId}` - Get user by ID
- `GET /api/v1/auth-demo/employees/{employeeId}/user` - Get user by employee ID
- `GET /api/v1/auth-demo/users` - Get all users (Admin only)

### 5. Service Layer Integration

**File:** `src/main/java/com/ibm/payroll/service/impl/PayrollServiceImpl.java`

Added `AuthClient` as a dependency to enable auth-service integration in business logic.

## Usage Examples

### Example 1: Login and Get Token

```java
@Autowired
private AuthClient authClient;

public void loginExample() {
    LoginRequestDto request = new LoginRequestDto("username", "password");
    Optional<LoginResponseDto> response = authClient.login(request);
    
    if (response.isPresent()) {
        String token = response.get().getToken();
        // Use token for subsequent requests
    }
}
```

### Example 2: Get Current User

```java
@Autowired
private AuthClient authClient;

public void getCurrentUserExample(String authHeader) {
    Optional<UserResponseDto> user = authClient.getCurrentUser(authHeader);
    
    if (user.isPresent()) {
        String employeeId = user.get().getEmployeeId();
        Set<String> roles = user.get().getRoles();
        // Use user details
    }
}
```

### Example 3: Get User by Employee ID

```java
@Autowired
private AuthClient authClient;

public void getUserByEmployeeIdExample(String employeeId, String authHeader) {
    Optional<UserResponseDto> user = authClient.getUserByEmployeeId(employeeId, authHeader);
    
    if (user.isPresent()) {
        String username = user.get().getUsername();
        String email = user.get().getEmail();
        // Use user details
    }
}
```

### Example 4: Integration in Service Layer

```java
@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final AuthClient authClient;
    private final EmployeeClient employeeClient;

    public PayrollDto processPayroll(String employeeId, String authHeader) {
        // Get user details from auth service
        Optional<UserResponseDto> user = authClient.getUserByEmployeeId(employeeId, authHeader);
        
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found for employee: " + employeeId);
        }

        // Get employee details from employee service
        Optional<EmployeeDto> employee = employeeClient.getEmployee(employeeId, authHeader);
        
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found: " + employeeId);
        }

        // Process payroll with both user and employee data
        return calculatePayroll(employee.get(), user.get());
    }
}
```

## Testing the Integration

### Prerequisites
1. Auth-service must be running on `http://localhost:8081`
2. Payroll-service must be running on `http://localhost:8083`

### Test Endpoints Using Postman/cURL

#### 1. Login via Payroll Service
```bash
curl -X POST http://localhost:8083/api/v1/auth-demo/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

#### 2. Get Current User
```bash
curl -X GET http://localhost:8083/api/v1/auth-demo/me \
  -H "Authorization: Bearer <your-jwt-token>"
```

#### 3. Get User by Employee ID
```bash
curl -X GET http://localhost:8083/api/v1/auth-demo/employees/EMP001/user \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Architecture Benefits

1. **Separation of Concerns**: Auth logic remains in auth-service
2. **Reusability**: AuthClient can be used across multiple services
3. **Type Safety**: Strongly typed DTOs prevent errors
4. **Error Handling**: Built-in logging and Optional return types
5. **Consistent Pattern**: Follows same pattern as EmployeeClient
6. **Scalability**: Easy to add more auth-service endpoints

## Error Handling

All AuthClient methods return `Optional<T>`:
- `Optional.empty()` indicates the request failed
- Errors are logged with appropriate messages
- Calling code should handle empty optionals gracefully

## Security Considerations

1. **JWT Token**: Always pass the full "Bearer <token>" in Authorization header
2. **HTTPS**: In production, use HTTPS for auth-service communication
3. **Token Validation**: Tokens are validated by auth-service
4. **Role-Based Access**: Some endpoints require specific roles (e.g., ADMIN)

## Future Enhancements

Potential additions to AuthClient:
- Password reset endpoints
- User registration endpoints
- Role management endpoints
- Token refresh functionality
- Circuit breaker pattern for resilience
- Caching for frequently accessed user data

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure auth-service is running on port 8081
   - Check `auth-service.base-url` in application.properties

2. **401 Unauthorized**
   - Verify JWT token is valid and not expired
   - Ensure "Bearer " prefix is included in Authorization header

3. **404 Not Found**
   - Verify the endpoint path in auth-service
   - Check auth-service logs for routing issues

4. **Empty Optional Returned**
   - Check auth-service logs for errors
   - Verify request payload format
   - Ensure auth-service is accessible

## Summary

The payroll-service now has full integration with auth-service through the AuthClient, enabling:
- User authentication
- User profile retrieval
- Employee-to-user mapping
- Role-based access control

This integration follows microservices best practices and maintains loose coupling between services.
