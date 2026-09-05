import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getCourse, getCourseProgress, getCourseSections, getSectionLectures, updateLectureProgress, getCourseQuizzes, getCourseAssignments } from '../../api/courseApi';

export default function CourseLearning() {
  const { courseId } = useParams();
  const [course, setCourse] = useState(null);
  const [sections, setSections] = useState([]);
  const [lectures, setLectures] = useState({});
  const [progress, setProgress] = useState(null);
  const [selected, setSelected] = useState(null);
  const [quizzes, setQuizzes] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    Promise.all([getCourse(courseId), getCourseSections(courseId), getCourseProgress(courseId), getCourseQuizzes(courseId), getCourseAssignments(courseId)])
      .then(async ([courseRes, sectionRes, progressRes, quizRes, assignmentRes]) => {
        setCourse(courseRes.data); setProgress(progressRes.data); setQuizzes(quizRes.data || []); setAssignments(assignmentRes.data || []);
        const sectionList = Array.isArray(sectionRes.data) ? sectionRes.data : sectionRes.data.content || [];
        setSections(sectionList);
        const entries = await Promise.all(sectionList.map(async (section) => [section.id, (await getSectionLectures(section.id)).data]));
        setLectures(Object.fromEntries(entries));
      })
      .catch(() => setMessage('Course content could not be loaded.'));
  }, [courseId]);

  const markComplete = async (lecture) => {
    try {
      const { data } = await updateLectureProgress(lecture.id, { watchedSeconds: lecture.durationSeconds || 0, completed: true });
      setProgress(data); setMessage('Lecture completed and progress updated.');
    } catch { setMessage('Could not update lecture progress.'); }
  };

  if (!course) return <main className="student-page"><p className="muted">Loading course...</p>{message && <p className="error-message">{message}</p>}</main>;

  return (
    <main className="learning-page">
      <header className="learning-header"><div><p className="eyebrow">Learning</p><h1>{course.title}</h1><p>{course.description}</p></div><div className="learning-progress"><strong>{Number(progress?.courseProgressPercentage ?? progress?.progressPercentage ?? 0).toFixed(0)}%</strong><span>Course progress</span></div></header>
      {message && <p className="success-message">{message}</p>}
      <div className="learning-layout">
        <aside className="lesson-sidebar"><h2>Course Content</h2>{sections.map((section) => <div className="lesson-section" key={section.id}><h3>{section.title}</h3>{(lectures[section.id] || []).map((lecture) => <button className={`lesson-item ${selected?.id === lecture.id ? 'active' : ''}`} key={lecture.id} onClick={() => setSelected(lecture)}>{lecture.title}</button>)}</div>)}</aside>
        <section className="lesson-content">{selected ? <><h2>{selected.title}</h2><p>{selected.description}</p>{selected.videoUrl ? <video className="lesson-video" src={selected.videoUrl} controls onEnded={() => markComplete(selected)} /> : <div className="video-placeholder">No video URL available.</div>}<button className="primary-button" onClick={() => markComplete(selected)}>Mark as Complete</button></> : <div className="empty-card"><h2>Select a lecture</h2><p>Choose a lesson from the course content to start learning.</p></div>}
          <div className="learning-extra"><div><h3>Quizzes</h3><p>{quizzes.length} published quiz(es)</p></div><div><h3>Assignments</h3><p>{assignments.length} published assignment(s)</p></div></div>
        </section>
      </div>
    </main>
  );
}
