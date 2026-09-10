package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 2000) private String questionText;
    @Column(nullable = false, length = 500) private String optionA;
    @Column(nullable = false, length = 500) private String optionB;
    @Column(nullable = false, length = 500) private String optionC;
    @Column(nullable = false, length = 500) private String optionD;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 1) private CorrectOption correctOption;
    @Column(nullable = false) @Builder.Default private Integer marks = 1;
    @Column(nullable = false) @Builder.Default private Integer displayOrder = 0;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "quiz_id", nullable = false) private Quiz quiz;
    public enum CorrectOption { A, B, C, D }
}
