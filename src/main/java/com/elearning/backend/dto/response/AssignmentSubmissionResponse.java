package com.elearning.backend.dto.response;

import com.elearning.backend.entity.AssignmentSubmission;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class AssignmentSubmissionResponse {
 public Long id; public Long assignmentId; public Long studentId; public String submissionUrl; public String submissionText; public LocalDateTime submittedAt; public Integer marks; public String feedback; public AssignmentSubmission.SubmissionStatus status;
 public static AssignmentSubmissionResponse fromEntity(AssignmentSubmission s){return AssignmentSubmissionResponse.builder().id(s.getId()).assignmentId(s.getAssignment().getId()).studentId(s.getStudent().getId()).submissionUrl(s.getSubmissionUrl()).submissionText(s.getSubmissionText()).submittedAt(s.getSubmittedAt()).marks(s.getMarks()).feedback(s.getFeedback()).status(s.getStatus()).build();}
}
