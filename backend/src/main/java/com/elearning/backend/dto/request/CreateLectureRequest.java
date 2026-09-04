package com.elearning.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLectureRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 3000)
    private String description;

    @NotBlank
    @Size(max = 1000)
    private String videoUrl;

    @NotNull
    @Min(0)
    private Integer durationSeconds;

    @NotNull
    private Integer displayOrder;

    private boolean preview;
}
