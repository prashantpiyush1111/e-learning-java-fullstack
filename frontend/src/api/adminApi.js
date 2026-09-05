import api from './axios';

export const getDashboard = () => api.get('/admin/dashboard');
export const getPendingCourses = () => api.get('/admin/courses/pending');
export const approveCourse = (courseId) => api.put(`/admin/courses/${courseId}/approve`);
export const rejectCourse = (courseId) => api.put(`/admin/courses/${courseId}/reject`);
export const getUsers = () => api.get('/admin/users');
export const updateUserStatus = (userId, payload) => api.put(`/admin/users/${userId}/status`, payload);
