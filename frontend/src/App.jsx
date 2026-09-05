import { Navigate, Route, Routes } from 'react-router-dom';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<div className="page-placeholder"><h1>E-Learning Platform</h1><p>Login module coming next.</p></div>} />
      <Route path="*" element={<div className="page-placeholder"><h1>404</h1><p>Page not found.</p></div>} />
    </Routes>
  );
}

export default App;
