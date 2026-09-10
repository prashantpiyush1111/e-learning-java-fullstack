package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.CreateCourseRequest;
import com.elearning.backend.dto.request.UpdateCourseRequest;
import com.elearning.backend.dto.response.CourseResponse;
import com.elearning.backend.entity.Course;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public CourseResponse createCourse(CreateCourseRequest request, String instructorEmail) {
        User instructor = getInstructor(instructorEmail);

        if (request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Course price cannot be negative");
        }

        Course course = Course.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .thumbnailUrl(normalize(request.getThumbnailUrl()))
                .status(Course.CourseStatus.PENDING)
                .instructor(instructor)
                .build();

        return CourseResponse.fromEntity(courseRepository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return CourseResponse.fromEntity(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getApprovedCourses(
            String search,
            String category,
            Pageable pageable) {

        String cleanSearch = search == null ? "" : search.trim();
        String cleanCategory = category == null ? "" : category.trim();

        Page<Course> courses;

        if (cleanSearch.isEmpty() && cleanCategory.isEmpty()) {
            courses = courseRepository.findByStatus(
                    Course.CourseStatus.APPROVED, pageable);
        } else if (cleanSearch.isEmpty()) {
            courses = courseRepository.findByStatusAndCategoryIgnoreCase(
                    Course.CourseStatus.APPROVED, cleanCategory, pageable);
        } else if (cleanCategory.isEmpty()) {
            courses = courseRepository.findByStatusAndTitleContainingIgnoreCase(
                    Course.CourseStatus.APPROVED, cleanSearch, pageable);
        } else {
            courses = courseRepository.findByStatusAndCategoryIgnoreCaseAndTitleContainingIgnoreCase(
                    Course.CourseStatus.APPROVED, cleanCategory, cleanSearch, pageable);
        }

        return courses.map(CourseResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getMyCourses(String instructorEmail) {
        User instructor = getInstructor(instructorEmail);

        return courseRepository.findByInstructorIdOrderByCreatedAtDesc(instructor.getId())
                .stream()
                .map(CourseResponse::fromEntity)
                .toList();
    }

    @Override
    public CourseResponse updateCourse(
            Long id,
            UpdateCourseRequest request,
            String instructorEmail) {

        User instructor = getInstructor(instructorEmail);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new BadRequestException("You can only modify your own courses");
        }

        course.setTitle(request.getTitle().trim());
        course.setDescription(request.getDescription().trim());
        course.setCategory(request.getCategory().trim());
        course.setPrice(request.getPrice());
        course.setThumbnailUrl(normalize(request.getThumbnailUrl()));

        // Any instructor edit sends the course back for admin review.
        course.setStatus(Course.CourseStatus.PENDING);

        return CourseResponse.fromEntity(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id, String instructorEmail) {
        User instructor = getInstructor(instructorEmail);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new BadRequestException("You can only delete your own courses");
        }

        courseRepository.delete(course);
    }

    private User getInstructor(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole().getName() != Role.RoleName.INSTRUCTOR) {
            throw new BadRequestException("Only instructors can manage courses");
        }

        return user;
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
