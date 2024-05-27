import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './AuthPage.css';
import { Link } from 'react-router-dom';

function SignUpPage({ switchToLoginPage }) {
  const [userAccount, setUserAccount] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSignUp = (e) => {
    e.preventDefault();
    // 여기서 회원가입 처리 로직을 추가.
    alert('회원가입이 완료되었습니다.');
  };

  return (
    <div className="auth-container">
      <h1 className="auth-title">회원가입</h1>
      <Form className="auth-form" onSubmit={handleSignUp}>
        <Form.Group controlId="formUserAccount">
          <Form.Label>User account *</Form.Label>
          <Form.Control
            type="text"
            value={userAccount}
            onChange={(e) => setUserAccount(e.target.value)}
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
        <Form.Group controlId="formConfirmPassword">
          <Form.Label>Password 확인 *</Form.Label>
          <Form.Control
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" style={{ float: 'right' }}>
          회원가입
        </Button>
      </Form>
      <div className="auth-footer">
        <Link to="/"><Button variant="link">홈화면</Button></Link>
        <Link to="/login"><Button variant="link">로그인하기</Button></Link>
      </div>
    </div>
  );
}

export default SignUpPage;
