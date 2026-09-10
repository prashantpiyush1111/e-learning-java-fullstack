package com.elearning.backend.controller;

import com.elearning.backend.dto.request.UpdateUserStatusRequest;
import com.elearning.backend.dto.response.AdminDashboardResponse;
import com.elearning.backend.dto.response.CourseResponse;
import com.elearning.backend.dto.response.UserResponse;
import com.elearning.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        return ResponseEntity.ok(adminService.dashboard());
    }

    @GetMapping("/courses/pending")
    public ResponseEntity<List<CourseResponse>> pendingCourses() {
        return ResponseEntity.ok(adminService.pendingCourses());
    }

    @PutMapping("/courses/{courseId}/approve")
    public ResponseEntity<CourseResponse> approveCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(adminService.approveCourse(courseId));
    }

    @PutMapping("/courses/{courseId}/reject")
    public ResponseEntity<CourseResponse> rejectCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(adminService.rejectCourse(courseId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> users() {
        return ResponseEntity.ok(adminService.users());
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(adminService.updateUserStatus(userId, request));
    }
}
