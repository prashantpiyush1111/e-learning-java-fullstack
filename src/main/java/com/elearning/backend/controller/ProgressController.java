package com.elearning.backend.controller;

import com.elearning.backend.dto.request.UpdateProgressRequest;
import com.elearning.backend.dto.response.LectureProgressResponse;
import com.elearning.backend.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @PutMapping("/lectures/{lectureId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LectureProgressResponse> updateProgress(
            @PathVariable Long lectureId,
            @Valid @RequestBody UpdateProgressRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(progressService.updateProgress(lectureId, request, authentication.getName()));
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<LectureProgressResponse>> getCourseProgress(
            @PathVariable Long courseId,
            Authentication authentication) {
        return ResponseEntity.ok(progressService.getCourseProgress(courseId, authentication.getName()));
    }
}
