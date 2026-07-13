package com.ibm.employee.controller;

import com.ibm.employee.security.LoggedInUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final LoggedInUser loggedInUser;

    @GetMapping("/api/v1/user/me")
    public String currentUser() {

        return loggedInUser.getUsername();
    }
}