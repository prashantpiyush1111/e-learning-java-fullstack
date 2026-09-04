package com.elearning.backend.repository;

import com.elearning.backend.entity.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureProgressRepository extends JpaRepository<LectureProgress, Long> {
    Optional<LectureProgress> findByEnrollmentIdAndLectureId(Long enrollmentId, Long lectureId);
    List<LectureProgress> findByEnrollmentId(Long enrollmentId);
}
