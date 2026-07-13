package com.ibm.employee.config;
import com.ibm.employee.security.JwtAuthenticationFilter;
import com.ibm.employee.security.JwtAuthenticationEntryPoint;
import com.ibm.employee.security.JwtAccessDeniedHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 
import java.util.List;
 
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
 
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAccessDeniedHandler accessDeniedHandler;
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
 
        http
                // Enable CORS
                .cors(Customizer.withDefaults())
 
                // Disable CSRF since we're using JWT
                .csrf(csrf -> csrf.disable())
 
                // Stateless session management
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
 
                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
 
                        // Allow CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
 
                        // Public endpoints
                        .requestMatchers(
//                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/actuator/health"
                        ).permitAll()
 
                        // Secure all other endpoints
                        .anyRequest().authenticated()
                )
 
                // Add JWT filter before Spring Security authentication filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
 
        return http.build();
    }
 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
 
        CorsConfiguration configuration = new CorsConfiguration();
 
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
 
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
 
        source.registerCorsConfiguration("/**", configuration);
 
        return source;
    }
}
 