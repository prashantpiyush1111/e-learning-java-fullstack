package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import com.elearning.backend.entity.*;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.*;
import com.elearning.backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
 private final QuizRepository quizRepository; private final QuizQuestionRepository questionRepository; private final QuizAttemptRepository attemptRepository; private final QuizAnswerRepository answerRepository; private final SectionRepository sectionRepository; private final UserRepository userRepository; private final EnrollmentRepository enrollmentRepository;
 private User user(String email){return userRepository.findByEmail(email.toLowerCase()).orElseThrow(()->new ResourceNotFoundException("User not found"));}
 private Quiz quiz(Long id){return quizRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quiz not found"));}
 private void instructor(User u){if(u.getRole().getName()!=Role.RoleName.INSTRUCTOR)throw new BadRequestException("Only instructors can manage quizzes");}
 private void enrolled(Quiz q,User u){if(!enrollmentRepository.existsByStudentIdAndCourseId(u.getId(),q.getSection().getCourse().getId()))throw new BadRequestException("Enroll in the course before attempting the quiz");}
 @Override public QuizResponse create(Long sectionId,CreateQuizRequest r,String email){User u=user(email);instructor(u);Section s=sectionRepository.findById(sectionId).orElseThrow(()->new ResourceNotFoundException("Section not found"));if(!s.getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");Quiz q=Quiz.builder().title(r.getTitle()).description(r.getDescription()).passingScore(r.getPassingScore()).timeLimitMinutes(r.getTimeLimitMinutes()).published(Boolean.TRUE.equals(r.getPublished())).section(s).build();return QuizResponse.fromEntity(quizRepository.save(q));}
 @Override public List<QuizResponse> sectionQuizzes(Long sectionId,String email){User u=user(email);return quizRepository.findBySectionIdOrderByIdAsc(sectionId).stream().filter(q->u.getRole().getName()!=Role.RoleName.STUDENT||Boolean.TRUE.equals(q.getPublished())).map(QuizResponse::fromEntity).collect(Collectors.toList());}
 @Override public List<QuizResponse> coursePublishedQuizzes(Long courseId){return quizRepository.findBySectionCourseIdAndPublishedTrueOrderByIdAsc(courseId).stream().map(QuizResponse::fromEntity).collect(Collectors.toList());}
 @Override public QuizQuestionResponse addQuestion(Long quizId,CreateQuizQuestionRequest r,String email){User u=user(email);instructor(u);Quiz q=quiz(quizId);if(!q.getSection().getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");QuizQuestion x=QuizQuestion.builder().questionText(r.getQuestionText()).optionA(r.getOptionA()).optionB(r.getOptionB()).optionC(r.getOptionC()).optionD(r.getOptionD()).correctOption(r.getCorrectOption()).marks(r.getMarks()).displayOrder(r.getDisplayOrder()).quiz(q).build();return QuizQuestionResponse.fromEntity(questionRepository.save(x));}
 @Override public List<QuizQuestionResponse> questions(Long quizId,String email){User u=user(email);Quiz q=quiz(quizId);if(u.getRole().getName()==Role.RoleName.STUDENT){if(!Boolean.TRUE.equals(q.getPublished()))throw new BadRequestException("Quiz is not published");enrolled(q,u);}return questionRepository.findByQuizIdOrderByDisplayOrderAsc(quizId).stream().map(QuizQuestionResponse::fromEntity).collect(Collectors.toList());}
 @Override @Transactional public QuizAttemptResponse submit(Long quizId,SubmitQuizRequest r,String email){User u=user(email);if(u.getRole().getName()!=Role.RoleName.STUDENT)throw new BadRequestException("Only students can submit quizzes");Quiz q=quiz(quizId);if(!Boolean.TRUE.equals(q.getPublished()))throw new BadRequestException("Quiz is not published");enrolled(q,u);List<QuizQuestion> qs=questionRepository.findByQuizIdOrderByDisplayOrderAsc(quizId);if(qs.isEmpty())throw new BadRequestException("Quiz has no questions");int total=qs.stream().mapToInt(QuizQuestion::getMarks).sum();int score=0;QuizAttempt a=QuizAttempt.builder().quiz(q).student(u).totalMarks(total).score(0.0).passed(false).submittedAt(LocalDateTime.now()).build();a=attemptRepository.save(a);for(QuizQuestion x:qs){QuizQuestion.CorrectOption selected=r.getAnswers().get(x.getId());int awarded=selected!=null&&selected==x.getCorrectOption()?x.getMarks():0;score+=awarded;answerRepository.save(QuizAnswer.builder().attempt(a).question(x).selectedOption(selected).awardedMarks(awarded).build());}double pct=total==0?0.0:(score*100.0/total);a.setScore(Math.round(pct*100.0)/100.0);a.setPassed(pct>=q.getPassingScore());return QuizAttemptResponse.fromEntity(attemptRepository.save(a));}
 @Override public List<QuizAttemptResponse> myAttempts(Long quizId,String email){User u=user(email);return attemptRepository.findByQuizIdAndStudentIdOrderBySubmittedAtDesc(quizId,u.getId()).stream().map(QuizAttemptResponse::fromEntity).collect(Collectors.toList());}
 @Override public void delete(Long quizId,String email){User u=user(email);instructor(u);Quiz q=quiz(quizId);if(!q.getSection().getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");quizRepository.delete(q);}
 @Override public void deleteQuestion(Long questionId,String email){User u=user(email);instructor(u);QuizQuestion q=questionRepository.findById(questionId).orElseThrow(()->new ResourceNotFoundException("Question not found"));if(!q.getQuiz().getSection().getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");questionRepository.delete(q);}
}
