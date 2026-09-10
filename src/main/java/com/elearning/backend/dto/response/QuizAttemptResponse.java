package com.elearning.backend.dto.response;

import com.elearning.backend.entity.QuizAttempt;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class QuizAttemptResponse {
 public Long id; public Long quizId; public Double score; public Integer totalMarks; public Boolean passed; public LocalDateTime startedAt; public LocalDateTime submittedAt;
 public static QuizAttemptResponse fromEntity(QuizAttempt a){return QuizAttemptResponse.builder().id(a.getId()).quizId(a.getQuiz().getId()).score(a.getScore()).totalMarks(a.getTotalMarks()).passed(a.getPassed()).startedAt(a.getStartedAt()).submittedAt(a.getSubmittedAt()).build();}
}
