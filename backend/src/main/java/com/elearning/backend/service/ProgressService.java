package com.elearning.backend.service;

import com.elearning.backend.dto.request.UpdateProgressRequest;
import com.elearning.backend.dto.response.LectureProgressResponse;

import java.util.List;

public interface ProgressService {
    LectureProgressResponse updateProgress(Long lectureId, UpdateProgressRequest request, String studentEmail);
    List<LectureProgressResponse> getCourseProgress(Long courseId, String studentEmail);
}
