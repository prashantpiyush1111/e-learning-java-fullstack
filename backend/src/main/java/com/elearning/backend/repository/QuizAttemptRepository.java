package com.elearning.backend.repository;

import com.elearning.backend.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt,Long> {
 List<QuizAttempt> findByQuizIdAndStudentIdOrderBySubmittedAtDesc(Long quizId,Long studentId);
}
