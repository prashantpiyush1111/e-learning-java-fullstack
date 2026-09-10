package com.elearning.backend.config;

import com.elearning.backend.entity.Role;
import com.elearning.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initializeRoles() {
        return args -> Arrays.stream(Role.RoleName.values()).forEach(roleName ->
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().name(roleName).build()
                        ))
        );
    }
}
