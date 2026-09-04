package com.elearning.backend.service;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import java.util.List;

public interface QuizService {
 QuizResponse create(Long sectionId, CreateQuizRequest request, String email);
 List<QuizResponse> sectionQuizzes(Long sectionId, String email);
 List<QuizResponse> coursePublishedQuizzes(Long courseId);
 QuizQuestionResponse addQuestion(Long quizId, CreateQuizQuestionRequest request, String email);
 List<QuizQuestionResponse> questions(Long quizId, String email);
 QuizAttemptResponse submit(Long quizId, SubmitQuizRequest request, String email);
 List<QuizAttemptResponse> myAttempts(Long quizId, String email);
 void delete(Long quizId, String email);
 void deleteQuestion(Long questionId, String email);
}
