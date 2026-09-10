package com.elearning.backend.service.impl;

import com.elearning.backend.dto.request.UpdateProgressRequest;
import com.elearning.backend.dto.response.LectureProgressResponse;
import com.elearning.backend.entity.Enrollment;
import com.elearning.backend.entity.Lecture;
import com.elearning.backend.entity.LectureProgress;
import com.elearning.backend.entity.Role;
import com.elearning.backend.entity.User;
import com.elearning.backend.exception.BadRequestException;
import com.elearning.backend.exception.ResourceNotFoundException;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.LectureProgressRepository;
import com.elearning.backend.repository.LectureRepository;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgressServiceImpl implements ProgressService {
    private final LectureProgressRepository progressRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Override
    public LectureProgressResponse updateProgress(Long lectureId, UpdateProgressRequest request, String studentEmail) {
        User student = getStudent(studentEmail);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found with id: " + lectureId));
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(
                        student.getId(), lecture.getSection().getCourse().getId())
                .orElseThrow(() -> new BadRequestException("Enroll in the course before tracking progress"));

        LectureProgress progress = progressRepository.findByEnrollmentIdAndLectureId(enrollment.getId(), lectureId)
                .orElseGet(() -> LectureProgress.builder().enrollment(enrollment).lecture(lecture).build());

        int duration = lecture.getDurationSeconds() == null ? 0 : lecture.getDurationSeconds();
        int watched = request.getWatchedSeconds();
        progress.setWatchedSeconds(watched);
        progress.setCompleted(request.isCompleted() || (duration > 0 && watched >= duration));
        progress = progressRepository.save(progress);

        refreshEnrollmentProgress(enrollment);
        return LectureProgressResponse.builder()
                .lectureId(progress.getLecture().getId())
                .watchedSeconds(progress.getWatchedSeconds())
                .completed(progress.isCompleted())
                .courseProgressPercentage(enrollment.getProgressPercentage())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LectureProgressResponse> getCourseProgress(Long courseId, String studentEmail) {
        User student = getStudent(studentEmail);
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return progressRepository.findByEnrollmentId(enrollment.getId()).stream()
                .map(p -> LectureProgressResponse.builder()
                        .lectureId(p.getLecture().getId())
                        .watchedSeconds(p.getWatchedSeconds())
                        .completed(p.isCompleted())
                        .courseProgressPercentage(enrollment.getProgressPercentage())
                        .build())
                .toList();
    }

    private void refreshEnrollmentProgress(Enrollment enrollment) {
        List<Lecture> lectures = lectureRepository.findBySectionCourseId(enrollment.getCourse().getId());
        long completedLectures = progressRepository.findByEnrollmentId(enrollment.getId()).stream()
                .filter(LectureProgress::isCompleted).count();
        double percentage = lectures.isEmpty() ? 0.0 : (completedLectures * 100.0) / lectures.size();
        enrollment.setProgressPercentage(Math.round(percentage * 100.0) / 100.0);
        enrollment.setCompleted(!lectures.isEmpty() && completedLectures == lectures.size());
        enrollmentRepository.save(enrollment);
    }

    private User getStudent(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == null || user.getRole().getName() != Role.RoleName.STUDENT) {
            throw new BadRequestException("Only students can track progress");
        }
        return user;
    }
}
