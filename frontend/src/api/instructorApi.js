import api from './axios';

export const getMyCourses = () => api.get('/courses/my');
export const createCourse = (payload) => api.post('/courses', payload);
export const updateCourse = (courseId, payload) => api.put(`/courses/${courseId}`, payload);
export const deleteCourse = (courseId) => api.delete(`/courses/${courseId}`);

export const getSections = (courseId) => api.get(`/courses/${courseId}/sections`);
export const createSection = (courseId, payload) => api.post(`/courses/${courseId}/sections`, payload);
export const updateSection = (courseId, sectionId, payload) => api.put(`/courses/${courseId}/sections/${sectionId}`, payload);
export const deleteSection = (courseId, sectionId) => api.delete(`/courses/${courseId}/sections/${sectionId}`);

export const getLectures = (sectionId) => api.get(`/sections/${sectionId}/lectures`);
export const createLecture = (sectionId, payload) => api.post(`/sections/${sectionId}/lectures`, payload);
export const updateLecture = (sectionId, lectureId, payload) => api.put(`/sections/${sectionId}/lectures/${lectureId}`, payload);
export const deleteLecture = (sectionId, lectureId) => api.delete(`/sections/${sectionId}/lectures/${lectureId}`);

export const getCourseQuizzes = (courseId) => api.get(`/courses/${courseId}/quizzes`);
export const getCourseAssignments = (courseId) => api.get(`/courses/${courseId}/assignments`);
