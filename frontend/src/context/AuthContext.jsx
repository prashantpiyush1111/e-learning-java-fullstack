import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, login as loginRequest, refreshToken as refreshTokenRequest, register as registerRequest } from '../api/authApi';

const AuthContext = createContext(null);
const ACCESS_TOKEN_KEY = 'elearning_access_token';
const REFRESH_TOKEN_KEY = 'elearning_refresh_token';
const USER_KEY = 'elearning_user';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem(USER_KEY)) || null; } catch { return null; }
  });
  const [accessToken, setAccessToken] = useState(() => localStorage.getItem(ACCESS_TOKEN_KEY));
  const [loading, setLoading] = useState(true);

  const persistSession = useCallback((data) => {
    if (data.accessToken) { localStorage.setItem(ACCESS_TOKEN_KEY, data.accessToken); setAccessToken(data.accessToken); }
    if (data.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken);
    if (data.user) { localStorage.setItem(USER_KEY, JSON.stringify(data.user)); setUser(data.user); }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setAccessToken(null);
    setUser(null);
  }, []);

  const login = useCallback(async (credentials) => {
    const response = await loginRequest(credentials);
    persistSession(response.data);
    return response.data;
  }, [persistSession]);

  const register = useCallback(async (payload) => {
    const response = await registerRequest(payload);
    persistSession(response.data);
    return response.data;
  }, [persistSession]);

  useEffect(() => {
    let active = true;
    const initialize = async () => {
      if (!localStorage.getItem(ACCESS_TOKEN_KEY)) { if (active) setLoading(false); return; }
      try {
        const response = await getCurrentUser();
        if (active) {
          const currentUser = response.data;
          localStorage.setItem(USER_KEY, JSON.stringify(currentUser));
          setUser(currentUser);
        }
      } catch {
        const refresh = localStorage.getItem(REFRESH_TOKEN_KEY);
        if (refresh) {
          try {
            const response = await refreshTokenRequest(refresh);
            if (active) persistSession(response.data);
          } catch { if (active) logout(); }
        } else if (active) logout();
      } finally { if (active) setLoading(false); }
    };
    initialize();
    return () => { active = false; };
  }, [logout, persistSession]);

  const value = useMemo(() => ({ user, accessToken, loading, login, register, logout, persistSession }), [user, accessToken, loading, login, register, logout, persistSession]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used inside AuthProvider');
  return context;
}

export { ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY };
