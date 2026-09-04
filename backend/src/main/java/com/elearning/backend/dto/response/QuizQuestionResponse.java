package com.elearning.backend.dto.response;

import com.elearning.backend.entity.QuizQuestion;
import lombok.*;

@Getter @Builder @AllArgsConstructor
public class QuizQuestionResponse {
 public Long id; public String questionText; public String optionA; public String optionB; public String optionC; public String optionD; public Integer marks; public Integer displayOrder;
 public static QuizQuestionResponse fromEntity(QuizQuestion q){return QuizQuestionResponse.builder().id(q.getId()).questionText(q.getQuestionText()).optionA(q.getOptionA()).optionB(q.getOptionB()).optionC(q.getOptionC()).optionD(q.getOptionD()).marks(q.getMarks()).displayOrder(q.getDisplayOrder()).build();}
}
