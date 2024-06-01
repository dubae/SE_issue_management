import React, { useState } from 'react';
import axios from 'axios';
import { Button, Form } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import 'bootstrap/dist/css/bootstrap.min.css';
import './AuthPage.css';

const API_URL = 'http://localhost:8080/api';

function LoginPage() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(`${API_URL}/login`, {
        userid: userId,
        password: password,
      });
      if (response.status === 200) {
        const isAdmin = response.data.isAdmin; // 관리자 여부를 응답에서 받아온다고 가정합니다.
        console.log(response.data.toString());
        const sessionId = response.data.sessionId; // 서버가 세션 ID를 반환한다고 가정합니다
        console.log(sessionId);
        // Store session ID in a cookie
        Cookies.set('sessionId', sessionId, { expires: 1 }); // 쿠키 만료 시간을 1일로 설정

        if (isAdmin) {
          alert('관리자 권한 허용되었습니다.');
        } else {
          alert('로그인이 완료되었습니다.');
        }
        localStorage.setItem('userId', userId);
        localStorage.setItem('isAdmin', isAdmin);
        navigate('/');
      } else {
        alert('로그인 실패. 아이디와 비밀번호를 확인하세요.');
      }
    } catch (error) {
      if (error.response) {
        // 서버가 클라이언트 오류에 대한 응답을 보냈을 때
        alert(error.response.data); // 에러 메시지 출력
      } else if (error.request) {
        // 요청이 이루어졌으나 응답을 받지 못함
        console.log(error.request);
      } else {
        // 요청 설정을 만드는 중에 문제가 발생
        console.log('Error', error.message);
      }
    }
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
