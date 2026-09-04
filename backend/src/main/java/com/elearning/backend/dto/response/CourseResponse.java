package com.elearning.backend.dto.response;

import com.elearning.backend.entity.Course;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private String thumbnailUrl;
    private String status;
    private Long instructorId;
    private String instructorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseResponse fromEntity(Course course) {
        String instructorName = course.getInstructor().getFirstName() + " "
                + course.getInstructor().getLastName();

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .thumbnailUrl(course.getThumbnailUrl())
                .status(course.getStatus().name())
                .instructorId(course.getInstructor().getId())
                .instructorName(instructorName.trim())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
