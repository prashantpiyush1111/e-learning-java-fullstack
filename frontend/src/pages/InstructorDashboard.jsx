import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getMyCourses } from '../api/instructorApi';

export default function InstructorDashboard() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getMyCourses()
      .then(({ data }) => setCourses(data || []))
      .catch(() => setError('Unable to load your courses.'))
      .finally(() => setLoading(false));
  }, []);

  const pending = courses.filter((c) => c.status === 'PENDING').length;
  const approved = courses.filter((c) => c.status === 'APPROVED').length;

  return (
    <main className="app-shell">
      <header className="topbar"><div><strong>E-Learning</strong><span className="muted"> Instructor Studio</span></div><Link className="btn" to="/instructor/courses/new">+ New Course</Link></header>
      <section className="content">
        <div className="page-heading"><div><p className="eyebrow">Instructor</p><h1>Dashboard</h1><p className="muted">Create, organize and manage your courses.</p></div></div>
        <div className="stats-grid"><div className="stat-card"><span>Total courses</span><b>{courses.length}</b></div><div className="stat-card"><span>Approved</span><b>{approved}</b></div><div className="stat-card"><span>Pending review</span><b>{pending}</b></div></div>
        {error && <div className="alert error">{error}</div>}
        <div className="section-title"><h2>Your courses</h2><Link to="/instructor/courses/new">Create course</Link></div>
        {loading ? <div className="empty-state">Loading courses...</div> : courses.length === 0 ? <div className="empty-state">No courses yet. Create your first course.</div> : <div className="course-grid">{courses.map((course) => <Link className="course-card" key={course.id} to={`/instructor/courses/${course.id}`}><div className="course-thumb">{course.thumbnailUrl ? <img src={course.thumbnailUrl} alt="" /> : <span>COURSE</span>}</div><div className="course-body"><span className={`badge ${course.status?.toLowerCase()}`}>{course.status}</span><h3>{course.title}</h3><p>{course.category}</p><strong>₹{course.price}</strong></div></Link>)}</div>}
      </section>
    </main>
  );
}
