import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './AuthPage.css';

function LoginPage() {
  const [userId, setUserId] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = (e) => {
    e.preventDefault();
    // 로그인 처리 로직을 추가.
    alert('로그인이 완료되었습니다.');
    localStorage.setItem('userId', userId);
  };

  return (
    <div className="auth-container">
      <h1 className="auth-title">로그인</h1>
      <Form className="auth-form" onSubmit={handleLogin}>
        <Form.Group controlId="formUserId">
          <Form.Label>ID *</Form.Label>
          <Form.Control
            type="text"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </Form.Group>
        <Form.Group controlId="formPassword">
          <Form.Label>Password *</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required    
          />
        </Form.Group>
        <Button variant="primary" type="submit" style={{ float: 'right' }}>
          로그인
        </Button>
      </Form>
      <div className="auth-footer">
        <Link to="/"><Button variant="link">홈화면</Button></Link>
        <Link to="/signup"><Button variant="link">회원가입</Button></Link>
      </div>
    </div>
  );
}

export default LoginPage;
