package com.elearning.backend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalStudents;
    private long totalInstructors;
    private long totalAdmins;
    private long totalCourses;
    private long pendingCourses;
    private long approvedCourses;
    private long rejectedCourses;
    private long totalEnrollments;
}
