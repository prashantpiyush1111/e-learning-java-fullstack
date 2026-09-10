package com.elearning.backend.dto.request;

import com.elearning.backend.entity.QuizQuestion.CorrectOption;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class SubmitQuizRequest {
 @NotEmpty private Map<Long,@NotNull CorrectOption> answers;
}
