import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage';
import ProjectDetailPage from './pages/ProjectDetailPage';
import IssueListPage from './pages/IssueListPage';
import IssueDetailPage from './pages/IssueDetailPage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignUpPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="signup" element={<SignupPage />} />
        <Route path="/project/:projectId" element={<ProjectDetailPage />} />
        <Route path="/project/:projectId/issues" element={<IssueListPage />} />
        <Route path="/project/:projectId/issues/:issueId" element={<IssueDetailPage />} />
      </Routes>
    </Router>
  );
}

export default App;
