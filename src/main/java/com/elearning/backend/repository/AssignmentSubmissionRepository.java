package com.elearning.backend.repository;

import com.elearning.backend.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission,Long> {
 Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId,Long studentId);
 List<AssignmentSubmission> findByAssignmentIdOrderBySubmittedAtDesc(Long assignmentId);
}
