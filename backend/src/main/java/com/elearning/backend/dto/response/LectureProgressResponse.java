package com.elearning.backend.dto.response;

import com.elearning.backend.entity.LectureProgress;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LectureProgressResponse {
    private Long lectureId;
    private Integer watchedSeconds;
    private boolean completed;
    private Double courseProgressPercentage;

    public static LectureProgressResponse fromEntity(LectureProgress p) {
        return LectureProgressResponse.builder()
                .lectureId(p.getLecture().getId())
                .watchedSeconds(p.getWatchedSeconds())
                .completed(p.isCompleted())
                .build();
    }
}
