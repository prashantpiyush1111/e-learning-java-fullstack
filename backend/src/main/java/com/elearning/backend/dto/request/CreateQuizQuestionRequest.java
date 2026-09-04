package com.elearning.backend.dto.request;

import com.elearning.backend.entity.QuizQuestion.CorrectOption;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateQuizQuestionRequest {
 @NotBlank @Size(max=2000) private String questionText;
 @NotBlank @Size(max=500) private String optionA;
 @NotBlank @Size(max=500) private String optionB;
 @NotBlank @Size(max=500) private String optionC;
 @NotBlank @Size(max=500) private String optionD;
 @NotNull private CorrectOption correctOption;
 @NotNull @Min(1) private Integer marks;
 @NotNull @Min(0) private Integer displayOrder;
}
