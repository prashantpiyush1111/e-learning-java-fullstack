import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { createCourse, getMyCourses, updateCourse } from '../api/instructorApi';

const empty = { title: '', description: '', category: '', price: 0, thumbnailUrl: '' };

export default function InstructorCourseForm() {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const editing = Boolean(courseId);
  const [form, setForm] = useState(empty);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!editing) return;
    getMyCourses().then(({ data }) => {
      const course = (data || []).find((item) => String(item.id) === String(courseId));
      if (course) setForm({ title: course.title || '', description: course.description || '', category: course.category || '', price: course.price ?? 0, thumbnailUrl: course.thumbnailUrl || '' });
      else setError('Course not found.');
    }).catch(() => setError('Unable to load course.'));
  }, [courseId, editing]);

  const onChange = (e) => setForm((current) => ({ ...current, [e.target.name]: e.target.value }));
  const submit = async (e) => {
    e.preventDefault(); setSaving(true); setError('');
    try { const { data } = editing ? await updateCourse(courseId, { ...form, price: Number(form.price) }) : await createCourse({ ...form, price: Number(form.price) }); navigate(`/instructor/courses/${data.id}`); }
    catch (err) { setError(err.response?.data?.message || 'Unable to save course.'); }
    finally { setSaving(false); }
  };

  return <main className="app-shell"><header className="topbar"><strong>E-Learning</strong><button className="link-button" onClick={() => navigate(-1)}>Back</button></header><section className="content narrow"><p className="eyebrow">Instructor Studio</p><h1>{editing ? 'Edit course' : 'Create a course'}</h1><p className="muted">Course changes return to pending review.</p>{error && <div className="alert error">{error}</div>}<form className="form-card" onSubmit={submit}><label>Title<input name="title" value={form.title} onChange={onChange} required maxLength="150" /></label><label>Description<textarea name="description" value={form.description} onChange={onChange} required rows="6" /></label><div className="form-row"><label>Category<input name="category" value={form.category} onChange={onChange} required /></label><label>Price (₹)<input name="price" type="number" min="0" step="0.01" value={form.price} onChange={onChange} required /></label></div><label>Thumbnail URL<input name="thumbnailUrl" value={form.thumbnailUrl} onChange={onChange} /></label><button className="btn" disabled={saving}>{saving ? 'Saving...' : editing ? 'Save changes' : 'Create course'}</button></form></section></main>;
}
