package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.UpdateProfileRequest;
import com.elearning.backend.dto.response.UserResponse;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = findUser(email);
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUser(String email, UpdateProfileRequest request) {
        User user = findUser(email);

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName().trim());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl().trim());
        }

        return UserResponse.fromEntity(userRepository.save(user));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
