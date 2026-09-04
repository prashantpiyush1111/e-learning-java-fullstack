package com.elearning.backend.controller;

import com.elearning.backend.dto.request.CreateLectureRequest;
import com.elearning.backend.dto.request.UpdateLectureRequest;
import com.elearning.backend.dto.response.LectureResponse;
import com.elearning.backend.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections/{sectionId}/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LectureResponse> createLecture(
            @PathVariable Long sectionId,
            @Valid @RequestBody CreateLectureRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lectureService.createLecture(sectionId, request, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<List<LectureResponse>> getLectures(@PathVariable Long sectionId) {
        return ResponseEntity.ok(lectureService.getLectures(sectionId));
    }

    @PutMapping("/{lectureId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LectureResponse> updateLecture(
            @PathVariable Long lectureId,
            @Valid @RequestBody UpdateLectureRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(lectureService.updateLecture(lectureId, request, authentication.getName()));
    }

    @DeleteMapping("/{lectureId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteLecture(
            @PathVariable Long lectureId,
            Authentication authentication) {
        lectureService.deleteLecture(lectureId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
