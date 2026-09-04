package com.elearning.backend.service;

import com.elearning.backend.dto.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(String email);
}
