package com.elearning.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatusRequest {
    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean accountNonLocked;
}
