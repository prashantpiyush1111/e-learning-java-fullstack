import { useEffect, useState } from 'react';
import { getUsers, updateUserStatus } from '../../api/adminApi';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

  const load = async () => {
    try { const response = await getUsers(); setUsers(response.data); setError(''); }
    catch (e) { setError(e.response?.data?.message || 'Unable to load users.'); }
  };
  useEffect(() => { load(); }, []);

  const toggle = async (user) => {
    try { await updateUserStatus(user.id, { enabled: !user.enabled, accountNonLocked: true }); await load(); }
    catch (e) { setError(e.response?.data?.message || 'Unable to update user status.'); }
  };

  return <main className="admin-page"><header className="admin-header"><div><p className="eyebrow">USER MANAGEMENT</p><h1>Platform Users</h1><p>Enable or disable non-admin accounts.</p></div></header>
    {error && <div className="alert error">{error}</div>}
    <section className="admin-section"><div className="admin-list">{users.map(user => <article className="admin-row" key={user.id}><div><h3>{user.firstName} {user.lastName}</h3><p>{user.email} · {user.role}</p></div><div className="row-actions"><span className={`status ${user.enabled ? 'approved' : 'rejected'}`}>{user.enabled ? 'ENABLED' : 'DISABLED'}</span>{user.role !== 'ADMIN' && <button className="button" onClick={() => toggle(user)}>{user.enabled ? 'Disable' : 'Enable'}</button>}</div></article>)}</div></section>
  </main>;
}
