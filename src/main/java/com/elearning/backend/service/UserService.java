package com.elearning.backend.service;

import com.elearning.backend.dto.request.UpdateProfileRequest;
import com.elearning.backend.dto.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(String email);
    UserResponse updateCurrentUser(String email, UpdateProfileRequest request);
}
