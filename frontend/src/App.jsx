import { Navigate, Route, Routes } from 'react-router-dom';
import { ProtectedRoute } from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import StudentDashboard from './pages/student/StudentDashboard';
import CourseCatalog from './pages/student/CourseCatalog';
import CourseLearning from './pages/student/CourseLearning';
import InstructorDashboard from './pages/InstructorDashboard';
import InstructorCourseForm from './pages/InstructorCourseForm';
import InstructorCourseManager from './pages/InstructorCourseManager';
import AdminDashboard from './pages/admin/AdminDashboard';
import AdminUsers from './pages/admin/AdminUsers';
import Login from './pages/Login';
import Profile from './pages/Profile';

function App() {
  return <><Navbar /><Routes>
    <Route path="/" element={<Navigate to="/login" replace />} />
    <Route path="/login" element={<Login />} />
    <Route element={<ProtectedRoute roles={["STUDENT"]} />}>
      <Route path="/dashboard" element={<StudentDashboard />} />
      <Route path="/courses" element={<CourseCatalog />} />
      <Route path="/courses/:courseId" element={<CourseLearning />} />
      <Route path="/profile" element={<Profile />} />
    </Route>
    <Route element={<ProtectedRoute roles={["INSTRUCTOR"]} />}>
      <Route path="/instructor/dashboard" element={<InstructorDashboard />} />
      <Route path="/instructor/courses/new" element={<InstructorCourseForm />} />
      <Route path="/instructor/courses/:courseId" element={<InstructorCourseManager />} />
      <Route path="/instructor/courses/:courseId/edit" element={<InstructorCourseForm />} />
      <Route path="/instructor/profile" element={<Profile />} />
    </Route>
    <Route element={<ProtectedRoute roles={["ADMIN"]} />}>
      <Route path="/admin/dashboard" element={<AdminDashboard />} />
      <Route path="/admin/users" element={<AdminUsers />} />
      <Route path="/admin/profile" element={<Profile />} />
    </Route>
    <Route path="*" element={<div className="page-placeholder"><h1>404</h1><p>Page not found.</p></div>} />
  </Routes></>;
}
export default App;
