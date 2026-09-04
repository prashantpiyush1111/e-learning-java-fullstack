package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.LoginRequest;
import com.elearning.backend.dto.request.RegisterRequest;
import com.elearning.backend.dto.response.AuthResponse;
import com.elearning.backend.dto.response.MessageResponse;
import com.elearning.backend.dto.response.UserResponse;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.UnauthorizedException;
import com.elearning.backend.repository.RoleRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.security.CustomUserDetails;
import com.elearning.backend.security.JwtService;
import com.elearning.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        Role.RoleName requestedRole = request.getRole() == null
                ? Role.RoleName.STUDENT
                : request.getRole();

        // Public registration cannot create an ADMIN account.
        // Instructor registration remains possible but should be approval-controlled later.
        if (requestedRole == Role.RoleName.ADMIN) {
            throw new BadRequestException("Admin accounts cannot be created through public registration");
        }

        Role role = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new BadRequestException("Requested role is not configured"));

        User user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        User savedUser = userRepository.save(user);
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        return buildAuthResponse(userDetails, savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        return buildAuthResponse(userDetails, user);
    }

    @Override
    public MessageResponse changePassword(
            String email,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New passwords do not match");
        }

        if (currentPassword.equals(newPassword)) {
            throw new BadRequestException("New password must be different from current password");
        }

        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new MessageResponse("Password changed successfully");
    }

    private AuthResponse buildAuthResponse(CustomUserDetails userDetails, User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .tokenType("Bearer")
                .user(UserResponse.fromEntity(user))
                .build();
    }
}
