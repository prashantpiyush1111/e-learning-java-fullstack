package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Section;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SectionResponse {
    private Long id;
    private String title;
    private String description;
    private Integer displayOrder;
    private Long courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SectionResponse fromEntity(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .description(section.getDescription())
                .displayOrder(section.getDisplayOrder())
                .courseId(section.getCourse().getId())
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .build();
    }
}
