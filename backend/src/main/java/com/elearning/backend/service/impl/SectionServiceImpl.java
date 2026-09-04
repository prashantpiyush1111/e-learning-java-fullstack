package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.CreateSectionRequest;
import com.elearning.backend.dto.request.UpdateSectionRequest;
import com.elearning.backend.dto.response.SectionResponse;
import com.elearning.backend.entity.Course;
import com.elearning.backend.entity.Section;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.SectionRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public SectionResponse createSection(Long courseId, CreateSectionRequest request, String instructorEmail) {
        Course course = getCourse(courseId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(course, instructor);

        Section section = Section.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .course(course)
                .build();
        return SectionResponse.fromEntity(sectionRepository.save(section));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionResponse> getSections(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return sectionRepository.findByCourseIdOrderByDisplayOrderAsc(courseId)
                .stream().map(SectionResponse::fromEntity).toList();
    }

    @Override
    public SectionResponse updateSection(Long sectionId, UpdateSectionRequest request, String instructorEmail) {
        Section section = getSection(sectionId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(section.getCourse(), instructor);

        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setDisplayOrder(request.getDisplayOrder());
        return SectionResponse.fromEntity(sectionRepository.save(section));
    }

    @Override
    public void deleteSection(Long sectionId, String instructorEmail) {
        Section section = getSection(sectionId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(section.getCourse(), instructor);
        sectionRepository.delete(section);
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private Section getSection(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
    }

    private User getInstructor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == null || user.getRole().getName() != com.elearning.backend.entity.Role.RoleName.INSTRUCTOR) {
            throw new BadRequestException("Only instructors can manage sections");
        }
        return user;
    }

    private void verifyOwnership(Course course, User instructor) {
        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new BadRequestException("You can only manage sections of your own courses");
        }
    }
}
