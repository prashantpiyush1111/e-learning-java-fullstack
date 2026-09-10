package com.elearning.backend.repository;

import com.elearning.backend.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
 List<Assignment> findBySectionIdOrderByIdAsc(Long sectionId);
 List<Assignment> findBySectionCourseIdAndPublishedTrueOrderByIdAsc(Long courseId);
 boolean existsByIdAndSectionCourseInstructorId(Long id,Long instructorId);
}
