package com.ibm.payroll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "goals")
public class Goal {

    @Id
    private String id;

    private String employeeId;

    private String title;

    private String description;

    private LocalDate targetDate;

    private String status;
}
