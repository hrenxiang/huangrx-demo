package com.huangrx.mystruct.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
@Data
@Builder
public class StudentDTO {
    private Long studentId;
    private String studentName;
    private Integer age;
    private String ageLevel;
    private String sexName;
    private LocalDate admissionDate;
}