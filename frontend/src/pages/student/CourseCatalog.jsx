import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getCourses } from '../../api/courseApi';

export default function CourseCatalog() {
  const [courses, setCourses] = useState([]);
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    getCourses({ search: search || undefined, category: category || undefined, page: 0, size: 12 })
      .then(({ data }) => setCourses(Array.isArray(data) ? data : data.content || []))
      .catch(() => setError('Unable to load courses.'))
      .finally(() => setLoading(false));
  }, [search, category]);

  return (
    <main className="student-page">
      <section className="catalog-header"><p className="eyebrow">Learn at your pace</p><h1>Explore Courses</h1><p>Find approved courses and start building practical skills.</p></section>
      <div className="catalog-filters">
        <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search courses..." aria-label="Search courses" />
        <input value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Filter by category" aria-label="Filter by category" />
      </div>
      {loading && <p className="muted">Loading courses...</p>}
      {error && <p className="error-message">{error}</p>}
      {!loading && !error && courses.length === 0 && <div className="empty-card"><h3>No matching courses</h3><p>Try another search or category.</p></div>}
      <div className="course-grid">
        {courses.map((course) => (
          <article className="course-card" key={course.id}>
            {course.thumbnailUrl ? <img src={course.thumbnailUrl} alt="" className="course-thumbnail" /> : <div className="course-thumbnail course-thumbnail-placeholder">COURSE</div>}
            <div className="course-card-body"><p className="course-category">{course.category}</p><h2>{course.title}</h2><p className="course-description">{course.description}</p><div className="course-meta"><strong>{course.price > 0 ? `₹${course.price}` : 'Free'}</strong><span>By {course.instructorName}</span></div><Link className="secondary-button" to={`/courses/${course.id}`}>View Course</Link></div>
          </article>
        ))}
      </div>
    </main>
  );
}
