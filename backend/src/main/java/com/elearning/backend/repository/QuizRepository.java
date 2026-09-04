package com.elearning.backend.repository;

import com.elearning.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
 List<Quiz> findBySectionIdOrderByIdAsc(Long sectionId);
 List<Quiz> findBySectionCourseIdAndPublishedTrueOrderByIdAsc(Long courseId);
 boolean existsByIdAndSectionCourseInstructorId(Long quizId,Long instructorId);
}
