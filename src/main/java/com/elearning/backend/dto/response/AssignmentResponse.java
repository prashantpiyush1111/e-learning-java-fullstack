package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Assignment;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class AssignmentResponse {
 public Long id; public String title; public String description; public String instructions; public Integer maxMarks; public LocalDateTime dueAt; public Boolean published; public Long sectionId;
 public static AssignmentResponse fromEntity(Assignment a){return AssignmentResponse.builder().id(a.getId()).title(a.getTitle()).description(a.getDescription()).instructions(a.getInstructions()).maxMarks(a.getMaxMarks()).dueAt(a.getDueAt()).published(a.getPublished()).sectionId(a.getSection().getId()).build();}
}
