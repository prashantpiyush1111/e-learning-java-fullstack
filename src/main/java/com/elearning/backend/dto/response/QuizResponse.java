package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Quiz;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class QuizResponse {
 public Long id; public String title; public String description; public Double passingScore; public Integer timeLimitMinutes; public Boolean published; public Long sectionId; public LocalDateTime createdAt; public LocalDateTime updatedAt;
 public static QuizResponse fromEntity(Quiz q){return QuizResponse.builder().id(q.getId()).title(q.getTitle()).description(q.getDescription()).passingScore(q.getPassingScore()).timeLimitMinutes(q.getTimeLimitMinutes()).published(q.getPublished()).sectionId(q.getSection().getId()).createdAt(q.getCreatedAt()).updatedAt(q.getUpdatedAt()).build();}
}
