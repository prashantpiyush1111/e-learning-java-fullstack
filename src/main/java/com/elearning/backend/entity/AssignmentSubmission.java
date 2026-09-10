package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="assignment_submissions",uniqueConstraints=@UniqueConstraint(columnNames={"assignment_id","student_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssignmentSubmission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="assignment_id",nullable=false) private Assignment assignment;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) private User student;
 @Column(length=500) private String submissionUrl;
 @Column(length=10000) private String submissionText;
 private LocalDateTime submittedAt;
 private Integer marks;
 @Column(length=5000) private String feedback;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) @Builder.Default private SubmissionStatus status=SubmissionStatus.SUBMITTED;
 public enum SubmissionStatus { SUBMITTED, GRADED }
}
