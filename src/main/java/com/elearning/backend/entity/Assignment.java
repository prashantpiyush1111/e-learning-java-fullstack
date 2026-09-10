package com.elearning.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=150) private String title;
 @Column(length=5000) private String description;
 @Column(length=5000) private String instructions;
 @Column(nullable=false) @Builder.Default private Integer maxMarks=100;
 private LocalDateTime dueAt;
 @Column(nullable=false) @Builder.Default private Boolean published=false;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="section_id",nullable=false) private Section section;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 @Column(nullable=false) private LocalDateTime updatedAt;
 @PrePersist protected void onCreate(){LocalDateTime n=LocalDateTime.now();createdAt=n;updatedAt=n;}
 @PreUpdate protected void onUpdate(){updatedAt=LocalDateTime.now();}
}
