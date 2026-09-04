package com.elearning.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SubmitAssignmentRequest {
 @Size(max=500) private String submissionUrl;
 @Size(max=10000) private String submissionText;
}
