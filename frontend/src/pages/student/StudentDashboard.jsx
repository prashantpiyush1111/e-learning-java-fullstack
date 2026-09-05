import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getMyEnrollments } from '../../api/courseApi';
import { useAuth } from '../../context/AuthContext';

export default function StudentDashboard() {
  const { user } = useAuth();
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getMyEnrollments()
      .then(({ data }) => setEnrollments(Array.isArray(data) ? data : data.content || []))
      .catch(() => setError('Unable to load your courses.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="student-page">
      <section className="student-hero">
        <div>
          <p className="eyebrow">Student Dashboard</p>
          <h1>Welcome back{user?.firstName ? `, ${user.firstName}` : ''}.</h1>
          <p>Continue learning and track your progress from one place.</p>
        </div>
        <Link className="primary-button" to="/courses">Browse Courses</Link>
      </section>

      <section className="dashboard-section">
        <div className="section-heading"><h2>My Learning</h2><span>{enrollments.length} course(s)</span></div>
        {loading && <p className="muted">Loading your courses...</p>}
        {error && <p className="error-message">{error}</p>}
        {!loading && !error && enrollments.length === 0 && (
          <div className="empty-card"><h3>No courses yet</h3><p>Explore the course catalog and start learning.</p><Link className="primary-button" to="/courses">Explore Courses</Link></div>
        )}
        <div className="course-grid">
          {enrollments.map((item) => (
            <article className="course-card" key={item.id}>
              <div className="course-card-body">
                <p className="course-category">Enrolled Course</p>
                <h3>{item.courseTitle}</h3>
                <div className="progress-row"><span>Progress</span><strong>{Number(item.progressPercentage || 0).toFixed(0)}%</strong></div>
                <div className="progress-track"><div className="progress-fill" style={{ width: `${Math.min(100, Math.max(0, Number(item.progressPercentage || 0)))}%` }} /></div>
                <Link className="secondary-button" to={`/courses/${item.courseId}`}>Continue Learning</Link>
              </div>
            </article>
          ))}
        </div>
      </section>
    </main>
  );
}
