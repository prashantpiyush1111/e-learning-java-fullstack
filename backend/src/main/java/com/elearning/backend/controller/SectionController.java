package com.elearning.backend.controller;

import com.elearning.backend.dto.request.CreateSectionRequest;
import com.elearning.backend.dto.request.UpdateSectionRequest;
import com.elearning.backend.dto.response.SectionResponse;
import com.elearning.backend.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateSectionRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sectionService.createSection(courseId, request, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable Long courseId) {
        return ResponseEntity.ok(sectionService.getSections(courseId));
    }

    @PutMapping("/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable Long sectionId,
            @Valid @RequestBody UpdateSectionRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(sectionService.updateSection(sectionId, request, authentication.getName()));
    }

    @DeleteMapping("/{sectionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long sectionId,
            Authentication authentication) {
        sectionService.deleteSection(sectionId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
