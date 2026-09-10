package com.elearning.backend.controller;

import com.elearning.backend.dto.request.ChangePasswordRequest;
import com.elearning.backend.dto.request.LoginRequest;
import com.elearning.backend.dto.request.RefreshTokenRequest;
import com.elearning.backend.dto.request.RegisterRequest;
import com.elearning.backend.dto.response.AuthResponse;
import com.elearning.backend.dto.response.MessageResponse;
import com.elearning.backend.dto.response.UserResponse;
import com.elearning.backend.service.AuthService;
import com.elearning.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(Map.of("accessToken", authService.refreshAccessToken(request.getRefreshToken())));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(authService.changePassword(
                authentication.getName(),
                request.getCurrentPassword(),
                request.getNewPassword(),
                request.getConfirmPassword()
        ));
    }
}
