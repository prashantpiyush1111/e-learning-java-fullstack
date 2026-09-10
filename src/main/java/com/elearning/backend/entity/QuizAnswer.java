package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="quiz_answers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizAnswer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="attempt_id",nullable=false) private QuizAttempt attempt;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="question_id",nullable=false) private QuizQuestion question;
 @Enumerated(EnumType.STRING) @Column(length=1) private QuizQuestion.CorrectOption selectedOption;
 @Column(nullable=false) @Builder.Default private Integer awardedMarks=0;
}
