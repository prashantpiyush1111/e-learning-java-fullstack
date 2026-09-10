package com.elearning.backend.service;

import com.elearning.backend.dto.request.CreateCourseRequest;
import com.elearning.backend.dto.request.UpdateCourseRequest;
import com.elearning.backend.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request, String instructorEmail);

    CourseResponse getCourseById(Long id);

    Page<CourseResponse> getApprovedCourses(String search, String category, Pageable pageable);

    List<CourseResponse> getMyCourses(String instructorEmail);

    CourseResponse updateCourse(Long id, UpdateCourseRequest request, String instructorEmail);

    void deleteCourse(Long id, String instructorEmail);
}
