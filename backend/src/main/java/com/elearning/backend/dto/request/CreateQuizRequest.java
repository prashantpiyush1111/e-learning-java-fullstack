package com.elearning.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateQuizRequest {
 @NotBlank @Size(max=150) private String title;
 @Size(max=2000) private String description;
 @NotNull @DecimalMin("0.0") @DecimalMax("100.0") private Double passingScore;
 @NotNull @Min(1) @Max(300) private Integer timeLimitMinutes;
 private Boolean published;
}
