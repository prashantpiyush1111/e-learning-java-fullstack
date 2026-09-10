package com.elearning.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProgressRequest {
    @NotNull
    @Min(0)
    private Integer watchedSeconds;

    private boolean completed;
}
