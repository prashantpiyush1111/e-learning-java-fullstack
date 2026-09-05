import { Navigate, Route, Routes } from 'react-router-dom';
import { ProtectedRoute } from './components/ProtectedRoute';
import StudentDashboard from './pages/student/StudentDashboard';
import CourseCatalog from './pages/student/CourseCatalog';
import CourseLearning from './pages/student/CourseLearning';

function LoginPlaceholder() {
  return <div className="page-placeholder"><h1>E-Learning Platform</h1><p>Login module coming next.</p></div>;
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPlaceholder />} />
      <Route element={<ProtectedRoute roles={["STUDENT"]} />}>
        <Route path="/dashboard" element={<StudentDashboard />} />
        <Route path="/courses" element={<CourseCatalog />} />
        <Route path="/courses/:courseId" element={<CourseLearning />} />
      </Route>
      <Route path="*" element={<div className="page-placeholder"><h1>404</h1><p>Page not found.</p></div>} />
    </Routes>
  );
}

export default App;
