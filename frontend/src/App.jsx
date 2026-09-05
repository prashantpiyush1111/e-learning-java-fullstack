import { Navigate, Route, Routes } from 'react-router-dom';
import { ProtectedRoute } from './components/ProtectedRoute';
import StudentDashboard from './pages/student/StudentDashboard';
import CourseCatalog from './pages/student/CourseCatalog';
import CourseLearning from './pages/student/CourseLearning';
import InstructorDashboard from './pages/InstructorDashboard';
import InstructorCourseForm from './pages/InstructorCourseForm';
import InstructorCourseManager from './pages/InstructorCourseManager';

function LoginPlaceholder() { return <div className="page-placeholder"><h1>E-Learning Platform</h1><p>Login module coming next.</p></div>; }

function App() {
  return <Routes>
    <Route path="/" element={<Navigate to="/login" replace />} />
    <Route path="/login" element={<LoginPlaceholder />} />
    <Route element={<ProtectedRoute roles={["STUDENT"]} />}>
      <Route path="/dashboard" element={<StudentDashboard />} />
      <Route path="/courses" element={<CourseCatalog />} />
      <Route path="/courses/:courseId" element={<CourseLearning />} />
    </Route>
    <Route element={<ProtectedRoute roles={["INSTRUCTOR"]} />}>
      <Route path="/instructor/dashboard" element={<InstructorDashboard />} />
      <Route path="/instructor/courses/new" element={<InstructorCourseForm />} />
      <Route path="/instructor/courses/:courseId" element={<InstructorCourseManager />} />
      <Route path="/instructor/courses/:courseId/edit" element={<InstructorCourseForm />} />
    </Route>
    <Route path="*" element={<div className="page-placeholder"><h1>404</h1><p>Page not found.</p></div>} />
  </Routes>;
}
export default App;
