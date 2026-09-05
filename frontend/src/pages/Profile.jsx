import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { updateCurrentUser } from '../api/authApi';

export default function Profile() {
  const { user, persistSession } = useAuth();
  const [form, setForm] = useState({ firstName: user?.firstName || '', lastName: user?.lastName || '', phone: user?.phone || '', bio: user?.bio || '', profileImageUrl: user?.profileImageUrl || '' });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const submit = async (e) => { e.preventDefault(); setSaving(true); setMessage(''); setError(''); try { const { data } = await updateCurrentUser(form); persistSession({ user: data }); setMessage('Profile updated successfully.'); } catch (err) { setError(err.response?.data?.message || 'Unable to update profile.'); } finally { setSaving(false); } };
  return <main className="profile-page"><section className="profile-card"><p className="eyebrow">ACCOUNT</p><h1>My Profile</h1><p className="muted">Update your personal information.</p><form className="profile-form" onSubmit={submit}>
    <div className="profile-grid"><label>First name<input required value={form.firstName} onChange={e => setForm({ ...form, firstName: e.target.value })} /></label><label>Last name<input required value={form.lastName} onChange={e => setForm({ ...form, lastName: e.target.value })} /></label><label>Email<input value={user?.email || ''} disabled /></label><label>Phone<input value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} /></label><label className="full">Profile image URL<input value={form.profileImageUrl} onChange={e => setForm({ ...form, profileImageUrl: e.target.value })} /></label><label className="full">Bio<textarea rows="5" value={form.bio} onChange={e => setForm({ ...form, bio: e.target.value })} /></label></div>
    {error && <div className="alert error">{error}</div>}{message && <div className="success-message">{message}</div>}<button className="primary-button" disabled={saving}>{saving ? 'Saving…' : 'Save changes'}</button>
  </form></section></main>;
}
