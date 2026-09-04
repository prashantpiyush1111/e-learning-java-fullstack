package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="quiz_attempts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizAttempt {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="quiz_id",nullable=false) private Quiz quiz;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="student_id",nullable=false) private User student;
 @Column(nullable=false) private Double score;
 @Column(nullable=false) private Integer totalMarks;
 @Column(nullable=false) private Boolean passed;
 @Column(nullable=false,updatable=false) private LocalDateTime startedAt;
 @Column(nullable=false) private LocalDateTime submittedAt;
 @PrePersist protected void onCreate(){ if(startedAt==null) startedAt=LocalDateTime.now(); }
}
