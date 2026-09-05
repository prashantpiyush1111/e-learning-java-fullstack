import { Navigate, Route, Routes } from 'react-router-dom';
import { ProtectedRoute } from './components/ProtectedRoute';

function LoginPlaceholder() {
  return <div className="page-placeholder"><h1>E-Learning Platform</h1><p>Login module coming next.</p></div>;
}

function HomePlaceholder() {
  return <div className="page-placeholder"><h1>Dashboard</h1><p>Authenticated area.</p></div>;
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPlaceholder />} />
      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<HomePlaceholder />} />
      </Route>
      <Route path="*" element={<div className="page-placeholder"><h1>404</h1><p>Page not found.</p></div>} />
    </Routes>
  );
}

export default App;
