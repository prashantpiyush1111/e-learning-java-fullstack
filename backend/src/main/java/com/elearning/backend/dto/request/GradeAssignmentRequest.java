package com.elearning.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeAssignmentRequest {
 @NotNull @Min(0) private Integer marks;
 @Size(max=5000) private String feedback;
}
