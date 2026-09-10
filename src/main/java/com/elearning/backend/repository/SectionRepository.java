package com.elearning.backend.repository;

import com.elearning.backend.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseIdOrderByDisplayOrderAsc(Long courseId);
    boolean existsByIdAndCourseInstructorId(Long sectionId, Long instructorId);
}
