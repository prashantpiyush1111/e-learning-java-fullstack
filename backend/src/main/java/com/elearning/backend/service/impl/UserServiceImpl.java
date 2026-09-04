package com.elearning.backend.service.impl;

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
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }
}
