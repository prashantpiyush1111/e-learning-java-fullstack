package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.UpdateUserStatusRequest;
import com.elearning.backend.dto.response.AdminDashboardResponse;
import com.elearning.backend.dto.response.CourseResponse;
import com.elearning.backend.dto.response.UserResponse;
import com.elearning.backend.entity.Course;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public AdminDashboardResponse dashboard() {
        return AdminDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalStudents(userRepository.countByRoleName(Role.RoleName.STUDENT))
                .totalInstructors(userRepository.countByRoleName(Role.RoleName.INSTRUCTOR))
                .totalAdmins(userRepository.countByRoleName(Role.RoleName.ADMIN))
                .totalCourses(courseRepository.count())
                .pendingCourses(courseRepository.countByStatus(Course.CourseStatus.PENDING))
                .approvedCourses(courseRepository.countByStatus(Course.CourseStatus.APPROVED))
                .rejectedCourses(courseRepository.countByStatus(Course.CourseStatus.REJECTED))
                .totalEnrollments(enrollmentRepository.count())
                .build();
    }

    @Override
    public List<CourseResponse> pendingCourses() {
        return courseRepository.findByStatusOrderByCreatedAtAsc(Course.CourseStatus.PENDING)
                .stream().map(CourseResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseResponse approveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (course.getStatus() == Course.CourseStatus.APPROVED) {
            throw new BadRequestException("Course is already approved");
        }
        course.setStatus(Course.CourseStatus.APPROVED);
        return CourseResponse.fromEntity(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponse rejectCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (course.getStatus() == Course.CourseStatus.REJECTED) {
            throw new BadRequestException("Course is already rejected");
        }
        course.setStatus(Course.CourseStatus.REJECTED);
        return CourseResponse.fromEntity(courseRepository.save(course));
    }

    @Override
    public List<UserResponse> users() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole().getName() == Role.RoleName.ADMIN &&
                (!Boolean.TRUE.equals(request.getEnabled()) || !Boolean.TRUE.equals(request.getAccountNonLocked()))) {
            throw new BadRequestException("Admin account cannot be disabled or locked");
        }
        user.setEnabled(request.getEnabled());
        user.setAccountNonLocked(request.getAccountNonLocked());
        return UserResponse.fromEntity(userRepository.save(user));
    }
}
