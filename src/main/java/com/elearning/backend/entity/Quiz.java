package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150) private String title;
    @Column(length = 2000) private String description;
    @Column(nullable = false) @Builder.Default private Double passingScore = 50.0;
    @Column(nullable = false) @Builder.Default private Integer timeLimitMinutes = 30;
    @Column(nullable = false) @Builder.Default private Boolean published = false;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "section_id", nullable = false)
    private Section section;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
    @PrePersist protected void onCreate(){ LocalDateTime n=LocalDateTime.now(); createdAt=n; updatedAt=n; }
    @PreUpdate protected void onUpdate(){ updatedAt=LocalDateTime.now(); }
}
