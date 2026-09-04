package com.elearning.backend.controller;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import com.elearning.backend.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class QuizController {
 private final QuizService service;
 @PostMapping("/sections/{sectionId}/quizzes") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<QuizResponse> create(@PathVariable Long sectionId,@Valid @RequestBody CreateQuizRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.create(sectionId,r,a.getName()));}
 @GetMapping("/sections/{sectionId}/quizzes") @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')") public ResponseEntity<List<QuizResponse>> section(@PathVariable Long sectionId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.sectionQuizzes(sectionId,a.getName()));}
 @GetMapping("/courses/{courseId}/quizzes") @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')") public ResponseEntity<List<QuizResponse>> course(@PathVariable Long courseId){return ResponseEntity.ok(service.coursePublishedQuizzes(courseId));}
 @PostMapping("/quizzes/{quizId}/questions") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<QuizQuestionResponse> addQuestion(@PathVariable Long quizId,@Valid @RequestBody CreateQuizQuestionRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.addQuestion(quizId,r,a.getName()));}
 @GetMapping("/quizzes/{quizId}/questions") @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')") public ResponseEntity<List<QuizQuestionResponse>> questions(@PathVariable Long quizId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.questions(quizId,a.getName()));}
 @PostMapping("/quizzes/{quizId}/attempts") @PreAuthorize("hasRole('STUDENT')") public ResponseEntity<QuizAttemptResponse> submit(@PathVariable Long quizId,@Valid @RequestBody SubmitQuizRequest r,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.submit(quizId,r,a.getName()));}
 @GetMapping("/quizzes/{quizId}/attempts/me") @PreAuthorize("hasRole('STUDENT')") public ResponseEntity<List<QuizAttemptResponse>> attempts(@PathVariable Long quizId,org.springframework.security.core.Authentication a){return ResponseEntity.ok(service.myAttempts(quizId,a.getName()));}
 @DeleteMapping("/quizzes/{quizId}/questions/{questionId}") @PreAuthorize("hasRole('INSTRUCTOR')") public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId,org.springframework.security.core.Authentication a){service.deleteQuestion(questionId,a.getName());return ResponseEntity.noContent().build();}
}
