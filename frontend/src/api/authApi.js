import api from './axios';

export const register = (payload) => api.post('/auth/register', payload);
export const login = (payload) => api.post('/auth/login', payload);
export const refreshToken = (refreshTokenValue) =>
  api.post('/auth/refresh', { refreshToken: refreshTokenValue });
export const changePassword = (payload) => api.post('/auth/change-password', payload);
export const getCurrentUser = () => api.get('/users/me');
export const updateCurrentUser = (payload) => api.put('/users/me', payload);
