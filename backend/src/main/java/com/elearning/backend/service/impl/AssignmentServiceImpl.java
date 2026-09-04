package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.*;
import com.elearning.backend.dto.response.*;
import com.elearning.backend.entity.*;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.*;
import com.elearning.backend.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
 private final AssignmentRepository assignmentRepository; private final AssignmentSubmissionRepository submissionRepository; private final SectionRepository sectionRepository; private final UserRepository userRepository; private final EnrollmentRepository enrollmentRepository;
 private User user(String email){return userRepository.findByEmail(email.toLowerCase()).orElseThrow(()->new ResourceNotFoundException("User not found"));}
 private Assignment assignment(Long id){return assignmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Assignment not found"));}
 private void instructor(User u){if(u.getRole().getName()!=Role.RoleName.INSTRUCTOR)throw new BadRequestException("Only instructors can manage assignments");}
 private void own(Assignment a,User u){if(!a.getSection().getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");}
 @Override public AssignmentResponse create(Long sectionId,CreateAssignmentRequest r,String email){User u=user(email);instructor(u);Section s=sectionRepository.findById(sectionId).orElseThrow(()->new ResourceNotFoundException("Section not found"));if(!s.getCourse().getInstructor().getId().equals(u.getId()))throw new BadRequestException("You do not own this course");Assignment a=Assignment.builder().title(r.getTitle()).description(r.getDescription()).instructions(r.getInstructions()).maxMarks(r.getMaxMarks()).dueAt(r.getDueAt()).published(Boolean.TRUE.equals(r.getPublished())).section(s).build();return AssignmentResponse.fromEntity(assignmentRepository.save(a));}
 @Override public List<AssignmentResponse> sectionAssignments(Long sectionId,String email){User u=user(email);return assignmentRepository.findBySectionIdOrderByIdAsc(sectionId).stream().filter(a->u.getRole().getName()!=Role.RoleName.STUDENT||Boolean.TRUE.equals(a.getPublished())).map(AssignmentResponse::fromEntity).collect(Collectors.toList());}
 @Override public List<AssignmentResponse> coursePublishedAssignments(Long courseId){return assignmentRepository.findBySectionCourseIdAndPublishedTrueOrderByIdAsc(courseId).stream().map(AssignmentResponse::fromEntity).collect(Collectors.toList());}
 @Override public AssignmentSubmissionResponse submit(Long assignmentId,SubmitAssignmentRequest r,String email){User u=user(email);if(u.getRole().getName()!=Role.RoleName.STUDENT)throw new BadRequestException("Only students can submit assignments");Assignment a=assignment(assignmentId);if(!Boolean.TRUE.equals(a.getPublished()))throw new BadRequestException("Assignment is not published");if(!enrollmentRepository.existsByStudentIdAndCourseId(u.getId(),a.getSection().getCourse().getId()))throw new BadRequestException("Enroll in the course before submitting");if(r.getSubmissionUrl()==null&&r.getSubmissionText()==null)throw new BadRequestException("Provide submission URL or text");if(a.getDueAt()!=null&&LocalDateTime.now().isAfter(a.getDueAt()))throw new BadRequestException("Assignment submission deadline has passed");AssignmentSubmission s=submissionRepository.findByAssignmentIdAndStudentId(assignmentId,u.getId()).orElse(AssignmentSubmission.builder().assignment(a).student(u).build());s.setSubmissionUrl(r.getSubmissionUrl());s.setSubmissionText(r.getSubmissionText());s.setSubmittedAt(LocalDateTime.now());s.setStatus(AssignmentSubmission.SubmissionStatus.SUBMITTED);s.setMarks(null);s.setFeedback(null);return AssignmentSubmissionResponse.fromEntity(submissionRepository.save(s));}
 @Override public AssignmentSubmissionResponse mySubmission(Long assignmentId,String email){User u=user(email);AssignmentSubmission s=submissionRepository.findByAssignmentIdAndStudentId(assignmentId,u.getId()).orElseThrow(()->new ResourceNotFoundException("Submission not found"));return AssignmentSubmissionResponse.fromEntity(s);}
 @Override public List<AssignmentSubmissionResponse> submissions(Long assignmentId,String email){User u=user(email);instructor(u);Assignment a=assignment(assignmentId);own(a,u);return submissionRepository.findByAssignmentIdOrderBySubmittedAtDesc(assignmentId).stream().map(AssignmentSubmissionResponse::fromEntity).collect(Collectors.toList());}
 @Override public AssignmentSubmissionResponse grade(Long submissionId,GradeAssignmentRequest r,String email){User u=user(email);instructor(u);AssignmentSubmission s=submissionRepository.findById(submissionId).orElseThrow(()->new ResourceNotFoundException("Submission not found"));own(s.getAssignment(),u);if(r.getMarks()>s.getAssignment().getMaxMarks())throw new BadRequestException("Marks cannot exceed maximum marks");s.setMarks(r.getMarks());s.setFeedback(r.getFeedback());s.setStatus(AssignmentSubmission.SubmissionStatus.GRADED);return AssignmentSubmissionResponse.fromEntity(submissionRepository.save(s));}
 @Override public void delete(Long assignmentId,String email){User u=user(email);instructor(u);Assignment a=assignment(assignmentId);own(a,u);if(!submissionRepository.findByAssignmentIdOrderBySubmittedAtDesc(assignmentId).isEmpty())throw new BadRequestException("Cannot delete an assignment with submissions");assignmentRepository.delete(a);}
}
