package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.CreateLectureRequest;
import com.elearning.backend.dto.request.UpdateLectureRequest;
import com.elearning.backend.dto.response.LectureResponse;
import com.elearning.backend.entity.Lecture;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.Section;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.LectureRepository;
import com.elearning.backend.repository.SectionRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    @Override
    public LectureResponse createLecture(Long sectionId, CreateLectureRequest request, String instructorEmail) {
        Section section = getSection(sectionId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(section, instructor);

        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .videoUrl(request.getVideoUrl())
                .durationSeconds(request.getDurationSeconds())
                .displayOrder(request.getDisplayOrder())
                .preview(request.isPreview())
                .section(section)
                .build();
        return LectureResponse.fromEntity(lectureRepository.save(lecture));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LectureResponse> getLectures(Long sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new ResourceNotFoundException("Section not found with id: " + sectionId);
        }
        return lectureRepository.findBySectionIdOrderByDisplayOrderAsc(sectionId)
                .stream().map(LectureResponse::fromEntity).toList();
    }

    @Override
    public LectureResponse updateLecture(Long lectureId, UpdateLectureRequest request, String instructorEmail) {
        Lecture lecture = getLecture(lectureId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(lecture.getSection(), instructor);

        lecture.setTitle(request.getTitle());
        lecture.setDescription(request.getDescription());
        lecture.setVideoUrl(request.getVideoUrl());
        lecture.setDurationSeconds(request.getDurationSeconds());
        lecture.setDisplayOrder(request.getDisplayOrder());
        lecture.setPreview(request.isPreview());
        return LectureResponse.fromEntity(lectureRepository.save(lecture));
    }

    @Override
    public void deleteLecture(Long lectureId, String instructorEmail) {
        Lecture lecture = getLecture(lectureId);
        User instructor = getInstructor(instructorEmail);
        verifyOwnership(lecture.getSection(), instructor);
        lectureRepository.delete(lecture);
    }

    private Section getSection(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
    }

    private Lecture getLecture(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found with id: " + id));
    }

    private User getInstructor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == null || user.getRole().getName() != Role.RoleName.INSTRUCTOR) {
            throw new BadRequestException("Only instructors can manage lectures");
        }
        return user;
    }

    private void verifyOwnership(Section section, User instructor) {
        if (!section.getCourse().getInstructor().getId().equals(instructor.getId())) {
            throw new BadRequestException("You can only manage lectures of your own courses");
        }
    }
}
