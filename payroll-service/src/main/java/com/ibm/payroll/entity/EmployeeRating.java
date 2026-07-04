package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "employee_ratings")
public class EmployeeRating {

    @Id
    private String id;

    private String employeeId;

    private String reviewId;

    private Double score;

    private String category;

    private LocalDateTime ratedAt;
}
