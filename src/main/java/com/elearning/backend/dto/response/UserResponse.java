package com.elearning.backend.dto.response;

import com.elearning.backend.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String bio;
    private String role;
    private boolean enabled;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .role(user.getRole().getName().name())
                .enabled(user.isEnabled())
                .build();
    }
}
