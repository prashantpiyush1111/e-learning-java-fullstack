package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Lecture;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LectureResponse {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private Integer durationSeconds;
    private Integer displayOrder;
    private boolean preview;
    private Long sectionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LectureResponse fromEntity(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .videoUrl(lecture.getVideoUrl())
                .durationSeconds(lecture.getDurationSeconds())
                .displayOrder(lecture.getDisplayOrder())
                .preview(lecture.isPreview())
                .sectionId(lecture.getSection().getId())
                .createdAt(lecture.getCreatedAt())
                .updatedAt(lecture.getUpdatedAt())
                .build();
    }
}
