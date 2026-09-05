import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function roleHome(role) {
  if (role === 'INSTRUCTOR') return '/instructor/dashboard';
  return '/dashboard';
}

export default function Register() {
  const { user, loading, register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', password: '', confirmPassword: '', phone: '', role: 'STUDENT'
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  if (!loading && user) return <Navigate to={roleHome(user.role?.name || user.role)} replace />;

  const update = (event) => setForm({ ...form, [event.target.name]: event.target.value });

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    setSubmitting(true);
    try {
      const data = await register(form);
      navigate(roleHome(data.user?.role), { replace: true });
    } catch (e) {
      setError(e.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return <main className="auth-page"><section className="auth-card">
    <p className="eyebrow">E-LEARNING PLATFORM</p>
    <h1>Create account</h1>
    <p className="auth-subtitle">Start your learning journey today.</p>
    <form onSubmit={submit} className="auth-form">
      <div className="auth-grid">
        <label>First name<input name="firstName" required maxLength="50" autoComplete="given-name" value={form.firstName} onChange={update} /></label>
        <label>Last name<input name="lastName" required maxLength="50" autoComplete="family-name" value={form.lastName} onChange={update} /></label>
      </div>
      <label>Email<input type="email" name="email" required maxLength="120" autoComplete="email" value={form.email} onChange={update} /></label>
      <label>Phone <span>(optional)</span><input name="phone" pattern="[0-9]{10}" maxLength="10" autoComplete="tel" value={form.phone} onChange={update} /></label>
      <label>Account type
        <select name="role" value={form.role} onChange={update}>
          <option value="STUDENT">Student</option>
          <option value="INSTRUCTOR">Instructor</option>
        </select>
      </label>
      <label>Password<input type="password" name="password" required minLength="8" maxLength="100" autoComplete="new-password" value={form.password} onChange={update} /></label>
      <label>Confirm password<input type="password" name="confirmPassword" required autoComplete="new-password" value={form.confirmPassword} onChange={update} /></label>
      {error && <div className="alert error">{error}</div>}
      <button className="primary-button auth-submit" disabled={submitting}>{submitting ? 'Creating account…' : 'Create account'}</button>
      <p className="auth-switch">Already have an account? <button type="button" onClick={() => navigate('/login')}>Sign in</button></p>
    </form>
  </section></main>;
}
