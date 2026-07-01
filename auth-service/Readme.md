# IBM EMS — Auth Service
### Knowledge Transfer Document

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Environment Setup](#environment-setup)
5. [How to Run](#how-to-run)
6. [Developer Task Distribution](#developer-task-distribution)
7. [API Reference](#api-reference)
8. [Security Flow](#security-flow)
9. [Database Collections](#database-collections)
10. [Common Errors & Fixes](#common-errors--fixes)

---

## Project Overview

The `auth-service` is a standalone microservice responsible for:

- User Registration & Login
- JWT Generation & Validation
- Role-Based Access Control (RBAC)
- Password Recovery (OTP via Email)
- User Management (CRUD, Enable/Disable)
- Role Assignment

This service is part of the **IBM Employee Management System (EMS)** capstone project, built using a microservices architecture.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.0 |
| Security | Spring Security 6.5.0 |
| Database | MongoDB Atlas |
| Authentication | JWT (jjwt 0.12.7) |
| Build Tool | Maven |
| API Docs | Springdoc OpenAPI (Swagger) 2.8.0 |
| Utilities | Lombok |

---

## Project Structure

```
auth-service
│
├── config
│     ├── PasswordConfig.java         # BCrypt Bean
│     ├── SecurityConfig.java         # Spring Security + JWT Filter
│     └── SwaggerConfig.java          # Swagger / OpenAPI config
│
├── constants
│     └── ApiRoutes.java              # All route constants
│
├── controller
│     ├── AuthController.java         # Signup, Login
│     ├── UserController.java         # User CRUD, Enable/Disable, Roles
│     └── PasswordController.java     # Forgot/Reset/Change Password
│
├── common
│     ├── payload
│     │     └── ApiResponse.java      # Generic response wrapper
│     └── exception
│           ├── GlobalExceptionHandler.java
│           ├── EmailAlreadyExistsException.java
│           ├── ForbiddenException.java
│           ├── InvalidCredentialsException.java
│           ├── OtpExpiredException.java
│           ├── OtpNotFoundException.java
│           ├── UnauthorizedException.java
│           ├── UsernameAlreadyExistsException.java
│           └── UserNotFoundException.java
│
├── entity
│     ├── User.java
│     └── Otp.java
│
├── payload
│     ├── enums
│     │     └── Role.java
│     ├── request
│     │     ├── SignupRequest.java
│     │     ├── LoginRequest.java
│     │     ├── ForgotPasswordRequest.java
│     │     ├── VerifyOtpRequest.java
│     │     ├── ResetPasswordRequest.java
│     │     ├── ChangePasswordRequest.java
│     │     ├── UpdateUserRequest.java
│     │     └── AssignRoleRequest.java
│     └── response
│           ├── LoginResponse.java
│           └── UserResponse.java
│
├── repository
│     ├── UserRepository.java
│     └── OtpRepository.java
│
├── security
│     ├── JwtUtil.java                # JWT Generate, Validate, Extract
│     ├── JwtFilter.java              # Intercepts every request
│     └── CustomUserDetailsService.java
│
├── service
│     ├── AuthService.java
│     ├── UserService.java
│     ├── OtpService.java
│     ├── EmailService.java
│     └── impl
│           ├── AuthServiceImpl.java
│           ├── UserServiceImpl.java
│           ├── OtpServiceImpl.java
│           └── EmailServiceImpl.java
│
└── AuthServiceApplication.java
```

---

## Environment Setup

### Prerequisites
- Java 17
- Maven
- IntelliJ IDEA
- MongoDB Atlas account

### application.properties
```properties
spring.application.name=auth-service
server.port=8080

# MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/auth_db?retryWrites=true&w=majority&appName=auth-ms-db

# JWT
jwt.secret=YourVeryLongSecretKeyThatMustBeAtLeast32CharactersLong123456
jwt.expiration=86400000

# Swagger
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.packagesToScan=com.ibm.auth
```

> ⚠️ Special characters in MongoDB password must be URL encoded:
> - `@` → `%40`
> - `#` → `%23`
> - `$` → `%24`

---

## How to Run

### First Time or After Config Changes
```bash
cd auth-service
mvn clean package -DskipTests
```
Then click the **Run** button in IntelliJ.

### Regular Java Code Changes
Just click the **Run** button directly.

### Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## Developer Task Distribution

### ✅ Developer 1 — Authentication (DONE)

**Responsible for:** Signup, Login, JWT, Spring Security

**Files Completed:**
- `config/PasswordConfig.java`
- `config/SecurityConfig.java`
- `security/JwtUtil.java`
- `security/JwtFilter.java`
- `security/CustomUserDetailsService.java`
- `service/AuthService.java`
- `service/impl/AuthServiceImpl.java`
- `controller/AuthController.java`
- `constants/ApiRoutes.java`

**APIs:**
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/auth/signup` | Public |
| POST | `/api/v1/auth/login` | Public |

---

### ✅ Developer 4 — Foundation (DONE)

**Responsible for:** Entities, Repositories, DTOs, Exceptions

**Files Completed:**
- `entity/User.java`
- `entity/Otp.java`
- `payload/enums/Role.java`
- `repository/UserRepository.java`
- `repository/OtpRepository.java`
- All Request DTOs
- All Response DTOs
- `common/payload/ApiResponse.java`
- All Custom Exceptions
- `common/exception/GlobalExceptionHandler.java`

---

### ⬜ Developer 2 — User Management (Read & Update)

**Responsible for:** Get and Update user APIs

**Files to Create:**
- `service/UserService.java`
- `service/impl/UserServiceImpl.java`
- `controller/UserController.java`

**APIs:**
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/v1/users` | ADMIN only |
| GET | `/api/v1/users/me` | Any authenticated user |
| GET | `/api/v1/users/{id}` | ADMIN or own profile |
| PUT | `/api/v1/users/{id}` | ADMIN or own profile |

---

### ⬜ Developer 3 — User Management (Admin Controls)

**Responsible for:** Delete, Enable/Disable, Role assignment

**Files to Extend:**
- `service/UserService.java` (add methods)
- `service/impl/UserServiceImpl.java` (add implementations)
- `controller/UserController.java` (add endpoints)

**APIs:**
| Method | Endpoint | Access |
|---|---|---|
| DELETE | `/api/v1/users/{id}` | ADMIN only |
| PUT | `/api/v1/users/{id}/roles` | ADMIN only |
| PUT | `/api/v1/users/{id}/enable` | ADMIN only |
| PUT | `/api/v1/users/{id}/disable` | ADMIN only |

> ⚠️ Delete is soft delete only — set `enabled=false`, do NOT remove from DB.

---

### ⬜ Developer 4 (Part 2) — Password Recovery

**Responsible for:** OTP, Email, Password reset flows

**Files to Create:**
- `service/OtpService.java`
- `service/impl/OtpServiceImpl.java`
- `service/EmailService.java`
- `service/impl/EmailServiceImpl.java`
- `controller/PasswordController.java`

**APIs:**
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/auth/forgot-password` | Public |
| POST | `/api/v1/auth/verify-otp` | Public |
| POST | `/api/v1/auth/reset-password` | Public |
| POST | `/api/v1/auth/change-password` | Authenticated |

---

## API Reference

### Auth APIs

#### POST `/api/v1/auth/signup`
**Request:**
```json
{
  "username": "rajeev",
  "email": "rajeev@gmail.com",
  "password": "Password@123",
  "confirmPassword": "Password@123"
}
```
**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null
}
```

---

#### POST `/api/v1/auth/login`
**Request:**
```json
{
  "username": "rajeev",
  "password": "Password@123"
}
```
**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "rajeev",
    "roles": ["ROLE_EMPLOYEE"]
  }
}
```

---

#### POST `/api/v1/auth/forgot-password`
**Request:**
```json
{
  "email": "rajeev@gmail.com"
}
```

---

#### POST `/api/v1/auth/verify-otp`
**Request:**
```json
{
  "email": "rajeev@gmail.com",
  "otp": "348591"
}
```

---

#### POST `/api/v1/auth/reset-password`
**Request:**
```json
{
  "email": "rajeev@gmail.com",
  "otp": "348591",
  "newPassword": "Password@123"
}
```

---

#### POST `/api/v1/auth/change-password`
> Requires JWT in Authorization header
```json
{
  "oldPassword": "OldPass@123",
  "newPassword": "NewPass@123"
}
```

---

### User APIs

#### GET `/api/v1/users`
> ADMIN only — Returns all users

#### GET `/api/v1/users/me`
> Any authenticated user — Returns logged-in user profile

#### GET `/api/v1/users/{id}`
> ADMIN or own profile

#### PUT `/api/v1/users/{id}`
**Request:**
```json
{
  "username": "rajeev_updated",
  "email": "rajeev_new@gmail.com"
}
```

#### DELETE `/api/v1/users/{id}`
> ADMIN only — Soft delete (sets enabled=false)

#### PUT `/api/v1/users/{id}/roles`
**Request:**
```json
{
  "roles": ["ROLE_MANAGER", "ROLE_EMPLOYEE"]
}
```

#### PUT `/api/v1/users/{id}/enable`
> ADMIN only

#### PUT `/api/v1/users/{id}/disable`
> ADMIN only

---

## Security Flow

### Signup Flow
```
POST /signup
     │
Validate Fields
     │
Username Exists? → throw UsernameAlreadyExistsException
     │
Email Exists? → throw EmailAlreadyExistsException
     │
Passwords Match? → throw InvalidCredentialsException
     │
Encode Password (BCrypt)
     │
Assign ROLE_EMPLOYEE
     │
Save to MongoDB
     │
Return Success
```

### Login Flow
```
POST /login
     │
AuthenticationManager.authenticate()
     │
CustomUserDetailsService.loadUserByUsername()
     │
MongoDB → Find User
     │
BCrypt password match
     │
Generate JWT
     │
Return token + username + roles
```

### Every Secured Request
```
Incoming Request
     │
JwtFilter intercepts
     │
Extract Bearer token
     │
JwtUtil.extractUsername()
     │
CustomUserDetailsService.loadUserByUsername()
     │
JwtUtil.validateToken()
     │
Set SecurityContext
     │
Continue to Controller
```

---

## Database Collections

### users
| Field | Type | Description |
|---|---|---|
| id | String | MongoDB ObjectId |
| username | String | Unique username |
| email | String | Unique email |
| password | String | BCrypt encoded |
| roles | Set\<Role\> | User roles |
| enabled | boolean | Account active status |
| accountLocked | boolean | Lock status |
| createdAt | LocalDateTime | Creation time |
| updatedAt | LocalDateTime | Last update time |

### otp
| Field | Type | Description |
|---|---|---|
| id | String | MongoDB ObjectId |
| email | String | User email |
| otp | String | 6-digit OTP |
| expiryTime | LocalDateTime | OTP expiry (10 min) |
| verified | boolean | OTP used status |
| createdAt | LocalDateTime | Creation time |

---

## Roles

| Role | Description |
|---|---|
| ROLE_ADMIN | Full access |
| ROLE_HR | HR operations |
| ROLE_MANAGER | Team management |
| ROLE_EMPLOYEE | Default role (assigned on signup) |
| ROLE_PAYROLL_ADMIN | Payroll operations |

---

## Common Errors & Fixes

| Error | Cause | Fix |
|---|---|---|
| `Connection refused localhost:27017` | MongoDB not running | Run `mvn clean package -DskipTests` after fixing URI |
| `application.properties not read` | Target folder stale | Run `mvn clean package -DskipTests` |
| `403 Forbidden on Swagger` | Spring Security blocking | Add Swagger URLs to `permitAll()` in SecurityConfig |
| `500 on /v3/api-docs` | Version mismatch | Use springdoc version `2.8.0` |
| `NoSuchMethodError ControllerAdviceBean` | springdoc incompatible | Upgrade springdoc to `2.8.0` |

---

## How to Use JWT in Swagger

1. Open `http://localhost:8080/swagger-ui/index.html`
2. Call `POST /api/v1/auth/login`
3. Copy the `token` from response
4. Click **Authorize** button (top right)
5. Enter: `Bearer <your_token>`
6. Click **Authorize**
7. Now all secured APIs will work

---

*IBM EMS Capstone Project — Auth Service KT Document*