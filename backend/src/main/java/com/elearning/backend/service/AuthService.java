package com.elearning.backend.service;

import com.elearning.backend.dto.request.LoginRequest;
import com.elearning.backend.dto.request.RegisterRequest;
import com.elearning.backend.dto.response.AuthResponse;
import com.elearning.backend.dto.response.MessageResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    MessageResponse changePassword(String email, String currentPassword, String newPassword, String confirmPassword);
}
