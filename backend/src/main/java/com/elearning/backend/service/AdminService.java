package com.elearning.backend.service;

import com.elearning.backend.dto.request.UpdateUserStatusRequest;
import com.elearning.backend.dto.response.AdminDashboardResponse;
import com.elearning.backend.dto.response.CourseResponse;
import com.elearning.backend.dto.response.UserResponse;

import java.util.List;

public interface AdminService {
    AdminDashboardResponse dashboard();
    List<CourseResponse> pendingCourses();
    CourseResponse approveCourse(Long courseId);
    CourseResponse rejectCourse(Long courseId);
    List<UserResponse> users();
    UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request);
}
