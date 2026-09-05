import { useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function roleHome(role) {
  if (role === 'ADMIN') return '/admin/dashboard';
  if (role === 'INSTRUCTOR') return '/instructor/dashboard';
  return '/dashboard';
}

export default function Login() {
  const { user, loading, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  if (!loading && user) return <Navigate to={roleHome(user.role?.name || user.role)} replace />;

  const submit = async (event) => {
    event.preventDefault(); setSubmitting(true); setError('');
    try {
      const data = await login(form);
      const requested = location.state?.from?.pathname;
      navigate(requested || roleHome(data.user?.role), { replace: true });
    } catch (e) {
      setError(e.response?.data?.message || 'Invalid email or password.');
    } finally { setSubmitting(false); }
  };

  return <main className="auth-page"><section className="auth-card"><p className="eyebrow">E-LEARNING PLATFORM</p><h1>Welcome back</h1><p className="auth-subtitle">Sign in to continue your learning journey.</p>
    <form onSubmit={submit} className="auth-form">
      <label>Email<input type="email" required autoComplete="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} /></label>
      <label>Password<input type="password" required autoComplete="current-password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} /></label>
      {error && <div className="alert error">{error}</div>}
      <button className="primary-button auth-submit" disabled={submitting}>{submitting ? 'Signing in…' : 'Sign in'}</button>
    </form>
  </section></main>;
}
