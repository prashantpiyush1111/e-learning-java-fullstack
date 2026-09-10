package com.elearning.backend.repository;

import com.elearning.backend.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {
 List<QuizQuestion> findByQuizIdOrderByDisplayOrderAsc(Long quizId);
 boolean existsByIdAndQuizSectionCourseInstructorId(Long questionId,Long instructorId);
}
