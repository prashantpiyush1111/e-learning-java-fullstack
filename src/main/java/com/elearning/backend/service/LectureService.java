package com.elearning.backend.service;

import com.elearning.backend.dto.request.CreateLectureRequest;
import com.elearning.backend.dto.request.UpdateLectureRequest;
import com.elearning.backend.dto.response.LectureResponse;

import java.util.List;

public interface LectureService {
    LectureResponse createLecture(Long sectionId, CreateLectureRequest request, String instructorEmail);
    List<LectureResponse> getLectures(Long sectionId);
    LectureResponse updateLecture(Long lectureId, UpdateLectureRequest request, String instructorEmail);
    void deleteLecture(Long lectureId, String instructorEmail);
}
