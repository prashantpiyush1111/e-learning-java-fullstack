package com.elearning.backend.service.impl;

import com.elearning.backend.dto.response.EnrollmentResponse;
import com.elearning.backend.entity.Course;
import com.elearning.backend.entity.Enrollment;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public EnrollmentResponse enroll(Long courseId, String studentEmail) {
        User student = getStudent(studentEmail);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (course.getStatus() != Course.CourseStatus.APPROVED) {
            throw new BadRequestException("Only approved courses can be enrolled in");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new BadRequestException("Already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder().student(student).course(course).build();
        return EnrollmentResponse.fromEntity(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollments(String studentEmail) {
        User student = getStudent(studentEmail);
        return enrollmentRepository.findByStudentIdOrderByEnrolledAtDesc(student.getId())
                .stream().map(EnrollmentResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollment(Long courseId, String studentEmail) {
        User student = getStudent(studentEmail);
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return EnrollmentResponse.fromEntity(enrollment);
    }

    private User getStudent(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == null || user.getRole().getName() != Role.RoleName.STUDENT) {
            throw new BadRequestException("Only students can enroll in courses");
        }
        return user;
    }
}
