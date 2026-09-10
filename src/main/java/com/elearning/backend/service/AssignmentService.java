package com.elearning.backend.service;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import java.util.List;

public interface AssignmentService {
 AssignmentResponse create(Long sectionId, CreateAssignmentRequest request, String email);
 List<AssignmentResponse> sectionAssignments(Long sectionId, String email);
 List<AssignmentResponse> coursePublishedAssignments(Long courseId);
 AssignmentSubmissionResponse submit(Long assignmentId, SubmitAssignmentRequest request, String email);
 AssignmentSubmissionResponse mySubmission(Long assignmentId, String email);
 List<AssignmentSubmissionResponse> submissions(Long assignmentId, String email);
 AssignmentSubmissionResponse grade(Long submissionId, GradeAssignmentRequest request, String email);
 void delete(Long assignmentId, String email);
}
