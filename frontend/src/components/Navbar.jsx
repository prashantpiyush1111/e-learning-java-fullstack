import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function home(role) {
  if (role === 'ADMIN') return '/admin/dashboard';
  if (role === 'INSTRUCTOR') return '/instructor/dashboard';
  return '/dashboard';
}

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  if (!user) return null;
  const role = user.role?.name || user.role;
  return <header className="navbar"><Link className="brand" to={home(role)}>E-Learning</Link><nav>
    {role === 'STUDENT' && <><Link to="/dashboard">Dashboard</Link><Link to="/courses">Courses</Link></>}
    {role === 'INSTRUCTOR' && <><Link to="/instructor/dashboard">Dashboard</Link><Link to="/instructor/courses/new">Create Course</Link></>}
    {role === 'ADMIN' && <><Link to="/admin/dashboard">Dashboard</Link><Link to="/admin/users">Users</Link></>}
    <button className="secondary-button" onClick={() => { logout(); navigate('/login', { replace: true }); }}>Logout</button>
  </nav></header>;
}
