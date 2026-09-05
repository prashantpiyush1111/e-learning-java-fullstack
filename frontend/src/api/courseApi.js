import api from './axios';

export const getCourses = (params = {}) => api.get('/courses', { params });
export const getCourse = (courseId) => api.get(`/courses/${courseId}`);
export const getMyEnrollments = () => api.get('/enrollments/me');
export const enrollInCourse = (courseId) => api.post(`/enrollments/courses/${courseId}`);
export const getEnrollment = (courseId) => api.get(`/enrollments/courses/${courseId}`);
export const getCourseSections = (courseId) => api.get(`/courses/${courseId}/sections`);
export const getSectionLectures = (sectionId) => api.get(`/sections/${sectionId}/lectures`);
export const updateLectureProgress = (lectureId, payload) => api.put(`/progress/lectures/${lectureId}`, payload);
export const getCourseProgress = (courseId) => api.get(`/progress/courses/${courseId}`);
export const getCourseQuizzes = (courseId) => api.get(`/courses/${courseId}/quizzes`);
export const getCourseAssignments = (courseId) => api.get(`/courses/${courseId}/assignments`);
