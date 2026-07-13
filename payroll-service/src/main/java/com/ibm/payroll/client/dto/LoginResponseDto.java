package com.ibm.payroll.client.dto;

import lombok.Data;
import java.util.Set;

@Data
public class LoginResponseDto {
    private String token;
    private String username;
    private Set<String> roles;
}
