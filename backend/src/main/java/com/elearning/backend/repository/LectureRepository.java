package com.elearning.backend.repository;

import com.elearning.backend.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findBySectionIdOrderByDisplayOrderAsc(Long sectionId);
    boolean existsByIdAndSectionCourseInstructorId(Long lectureId, Long instructorId);
}
