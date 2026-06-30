package com.ibm.auth.service.impl;

import com.ibm.auth.common.exception.EmailAlreadyExistsException;
import com.ibm.auth.common.exception.UserNotFoundException;
import com.ibm.auth.common.exception.UsernameAlreadyExistsException;
import com.ibm.auth.common.payload.ApiResponse;
import com.ibm.auth.entity.User;
import com.ibm.auth.payload.enums.Role;
import com.ibm.auth.payload.request.UpdateUserRequest;
import com.ibm.auth.payload.response.UserResponse;
import com.ibm.auth.repository.UserRepository;
import com.ibm.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Entity -> DTO
     */
    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .build();
    }

    /**
     * Get All Users
     */
    @Override
    public ApiResponse<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Users fetched successfully",
                users
        );
    }

    /**
     * Get Logged-in User
     */
    @Override
    public ApiResponse<UserResponse> getCurrentUser() {

        User user = getLoggedInUser();

        return new ApiResponse<>(
                true,
                "User fetched successfully",
                mapToResponse(user)
        );
    }

    /**
     * Get User By Id
     */
    @Override
    public ApiResponse<UserResponse> getUserById(String id) {

        User loggedInUser = getLoggedInUser();

        User targetUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!isAdmin(loggedInUser)
                && !loggedInUser.getId().equals(id)) {

            throw new RuntimeException("Access Denied");
        }

        return new ApiResponse<>(
                true,
                "User fetched successfully",
                mapToResponse(targetUser)
        );
    }

    /**
     * Update User
     */
    @Override
    public ApiResponse<UserResponse> updateUser(
            String id,
            UpdateUserRequest request) {

        User loggedInUser = getLoggedInUser();

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!isAdmin(loggedInUser)
                && !loggedInUser.getId().equals(id)) {

            throw new RuntimeException("Access Denied");
        }

        // Username validation
        if (request.getUsername() != null
                && !request.getUsername().equals(user.getUsername())) {

            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistsException(
                        "Username already exists");
            }

            user.setUsername(request.getUsername());
        }

        // Email validation
        if (request.getEmail() != null
                && !request.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already exists");
            }

            user.setEmail(request.getEmail());
        }

        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new ApiResponse<>(
                true,
                "User updated successfully",
                mapToResponse(savedUser)
        );
    }

    /**
     * Logged-in User
     */
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    /**
     * Check ADMIN Role
     */
    private boolean isAdmin(User user) {

        return user.getRoles()
                .contains(Role.ROLE_ADMIN);
    }

}