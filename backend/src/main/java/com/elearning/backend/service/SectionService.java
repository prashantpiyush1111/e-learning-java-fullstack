package com.elearning.backend.service;

import com.elearning.backend.dto.request.CreateSectionRequest;
import com.elearning.backend.dto.request.UpdateSectionRequest;
import com.elearning.backend.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {
    SectionResponse createSection(Long courseId, CreateSectionRequest request, String instructorEmail);
    List<SectionResponse> getSections(Long courseId);
    SectionResponse updateSection(Long sectionId, UpdateSectionRequest request, String instructorEmail);
    void deleteSection(Long sectionId, String instructorEmail);
}
