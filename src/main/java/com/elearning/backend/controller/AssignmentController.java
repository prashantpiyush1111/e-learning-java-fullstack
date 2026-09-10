package com.elearning.backend.controller;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import com.elearning.backend.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class AssignmentController {
 private final AssignmentService service;
 @PostMapping("/sections/{sectionId}/assignments") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<AssignmentResponse> create(@PathVariable Long sectionId,@Valid @RequestBody CreateAssignmentRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.create(sectionId,r,a.getName()));}
 @GetMapping("/sections/{sectionId}/assignments") @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')") public ResponseEntity<List<AssignmentResponse>> section(@PathVariable Long sectionId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.sectionAssignments(sectionId,a.getName()));}
 @GetMapping("/courses/{courseId}/assignments") @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')") public ResponseEntity<List<AssignmentResponse>> course(@PathVariable Long courseId){return ResponseEntity.ok(service.coursePublishedAssignments(courseId));}
 @PostMapping("/assignments/{assignmentId}/submissions") @PreAuthorize("hasRole('STUDENT')") public ResponseEntity<AssignmentSubmissionResponse> submit(@PathVariable Long assignmentId,@Valid @RequestBody SubmitAssignmentRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.submit(assignmentId,r,a.getName()));}
 @GetMapping("/assignments/{assignmentId}/submissions/me") @PreAuthorize("hasRole('STUDENT')") public ResponseEntity<AssignmentSubmissionResponse> my(@PathVariable Long assignmentId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.mySubmission(assignmentId,a.getName()));}
 @GetMapping("/assignments/{assignmentId}/submissions") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<List<AssignmentSubmissionResponse>> submissions(@PathVariable Long assignmentId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.submissions(assignmentId,a.getName()));}
 @PutMapping("/assignment-submissions/{submissionId}/grade") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<AssignmentSubmissionResponse> grade(@PathVariable Long submissionId,@Valid @RequestBody GradeAssignmentRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.grade(submissionId,r,a.getName()));}
 @DeleteMapping("/assignments/{assignmentId}") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<Void> delete(@PathVariable Long assignmentId,org.springframework.security.core.Authentication a){service.delete(assignmentId,a.getName());return ResponseEntity.noContent().build();}
}
