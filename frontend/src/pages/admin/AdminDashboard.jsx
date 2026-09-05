import { useEffect, useState } from 'react';
import { approveCourse, getDashboard, getPendingCourses, rejectCourse } from '../../api/adminApi';

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [courses, setCourses] = useState([]);
  const [error, setError] = useState('');

  const load = async () => {
    try {
      const [dashboardRes, coursesRes] = await Promise.all([getDashboard(), getPendingCourses()]);
      setStats(dashboardRes.data);
      setCourses(coursesRes.data);
      setError('');
    } catch (e) {
      setError(e.response?.data?.message || 'Unable to load admin dashboard.');
    }
  };

  useEffect(() => { load(); }, []);

  const review = async (id, action) => {
    try {
      if (action === 'approve') await approveCourse(id);
      else await rejectCourse(id);
      await load();
    } catch (e) {
      setError(e.response?.data?.message || 'Course review failed.');
    }
  };

  return (
    <main className="admin-page">
      <header className="admin-header">
        <div><p className="eyebrow">ADMIN CONSOLE</p><h1>Platform Overview</h1><p>Manage courses and keep the learning platform healthy.</p></div>
      </header>
      {error && <div className="alert error">{error}</div>}
      <section className="stat-grid">
        {stats && Object.entries({
          'Total Users': stats.totalUsers, Students: stats.totalStudents, Instructors: stats.totalInstructors,
          Courses: stats.totalCourses, Pending: stats.pendingCourses, Enrollments: stats.totalEnrollments
        }).map(([label, value]) => <article className="stat-card" key={label}><span>{label}</span><strong>{value}</strong></article>)}
      </section>
      <section className="admin-section">
        <div className="section-heading"><div><h2>Pending Courses</h2><p>Review instructor submissions before publishing.</p></div></div>
        {!courses.length ? <div className="empty-state">No pending courses.</div> : <div className="admin-list">
          {courses.map((course) => <article className="admin-row" key={course.id}>
            <div><h3>{course.title}</h3><p>{course.category} · {course.instructorName}</p><small>{course.description}</small></div>
            <div className="row-actions"><button className="button primary" onClick={() => review(course.id, 'approve')}>Approve</button><button className="button danger" onClick={() => review(course.id, 'reject')}>Reject</button></div>
          </article>)}
        </div>}
      </section>
    </main>
  );
}
