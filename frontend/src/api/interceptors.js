import api from './axios';
import { ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY } from '../context/AuthContext';

let refreshPromise = null;

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(ACCESS_TOKEN_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (error.response?.status !== 401 || original?._retry || original?.url?.includes('/auth/refresh')) {
      return Promise.reject(error);
    }

    const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);
    if (!refreshToken) return Promise.reject(error);

    original._retry = true;
    try {
      refreshPromise ??= api.post('/auth/refresh', { refreshToken }).finally(() => { refreshPromise = null; });
      const response = await refreshPromise;
      const newAccessToken = response.data.accessToken;
      if (!newAccessToken) throw new Error('Refresh response did not contain an access token');
      localStorage.setItem(ACCESS_TOKEN_KEY, newAccessToken);
      original.headers.Authorization = `Bearer ${newAccessToken}`;
      return api(original);
    } catch (refreshError) {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      localStorage.removeItem(REFRESH_TOKEN_KEY);
      localStorage.removeItem('elearning_user');
      window.location.href = '/login';
      return Promise.reject(refreshError);
    }
  }
);
