package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Enrollment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private Double progressPercentage;
    private boolean completed;
    private LocalDateTime enrolledAt;
    private LocalDateTime updatedAt;

    public static EnrollmentResponse fromEntity(Enrollment e) {
        return EnrollmentResponse.builder()
                .id(e.getId())
                .courseId(e.getCourse().getId())
                .courseTitle(e.getCourse().getTitle())
                .progressPercentage(e.getProgressPercentage())
                .completed(e.isCompleted())
                .enrolledAt(e.getEnrolledAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
