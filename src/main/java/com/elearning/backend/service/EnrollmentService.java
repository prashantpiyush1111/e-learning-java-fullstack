package com.elearning.backend.service;

import com.elearning.backend.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enroll(Long courseId, String studentEmail);
    List<EnrollmentResponse> getMyEnrollments(String studentEmail);
    EnrollmentResponse getEnrollment(Long courseId, String studentEmail);
}
