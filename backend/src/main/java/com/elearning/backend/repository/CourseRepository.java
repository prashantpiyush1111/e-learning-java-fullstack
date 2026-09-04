package com.elearning.backend.repository;

import com.elearning.backend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable);

    Page<Course> findByStatusAndCategoryIgnoreCase(
            Course.CourseStatus status,
            String category,
            Pageable pageable
    );

    Page<Course> findByStatusAndTitleContainingIgnoreCase(
            Course.CourseStatus status,
            String title,
            Pageable pageable
    );

    Page<Course> findByStatusAndCategoryIgnoreCaseAndTitleContainingIgnoreCase(
            Course.CourseStatus status,
            String category,
            String title,
            Pageable pageable
    );

    List<Course> findByInstructorIdOrderByCreatedAtDesc(Long instructorId);

    boolean existsByIdAndInstructorId(Long id, Long instructorId);
}
