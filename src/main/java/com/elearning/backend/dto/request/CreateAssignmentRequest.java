package com.elearning.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAssignmentRequest {
 @NotBlank @Size(max=150) private String title;
 @Size(max=5000) private String description;
 @Size(max=5000) private String instructions;
 @NotNull @Min(1) private Integer maxMarks;
 private LocalDateTime dueAt;
 private Boolean published;
}
