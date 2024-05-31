/*
React는 세션을 사용할 수 없음.
-> JWT 복잡함, 시간 없음
-> 서버에서 세션을 사용하도록 함
-> 서버로 로그인 요청을 보내고 로그인이 되면 서버에서 세션 문자열을 하나 만들어서
    클라이언트에게 보내주는 방식으로 구현
    
    localStorage.setItem('sessionid', response.data);
    코드가 다음과 같이 받은 로그인 엔드포인트로부터 받은 응답을 localStorage에 저장하고 다른 요청을 할때 꺼내서 사용
    로그인 한 후 다른 요청을 보낼 땐 이 세션 문자열을 헤더에 담아서 보내주면 됨
    로그아웃 하시면 localStorage에서 삭제하면 됨
    
    localStorage.getItem('sessionid');
    const response = await axios.post('http://localhost:8080/api/projects', {보낼 data}, {
      headers: {
        'sessionid': `${sessionid}`
      },
        withCredentials: true
      });

    로그인을 마치면 다른 엔드포인트에는 다음과 같은 방식으로 보내주시면 됩니다.
    헤더에 sessionid를 담아서 보내주시면 됩니다.
    서버가 이 세션 문자열을 받아서 세션을 확인하고 로그인 여부를 판단합니다.

    프론트 껐다가 실행하면 로그인이 유지되던데 이건 세션 때문에 로그인 풀리도록 해주셔야 합니다.
    
*/

import React, { useState } from 'react';
import axios from 'axios';
import { Button, Form } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './AuthPage.css';

const API_URL = 'http://localhost:8080/api';

function LoginPage() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState('');
  const [password, setPassword] = useState('');
  let isAuthenticated;
  let sessionid;

  const handleLogin = async (e) => {
    e.preventDefault();
    if (isAuthenticated === false){
      sessionid = null;
    }else{
      sessionid = localStorage.getItem('sessionId');
    }
    try {
      const response = await axios.post(`${API_URL}/login`, {
        userid: userId,
        password: password
    }, {
      headers: {
        'sessionid': `${sessionid}`
      },
      withCredentials: true
    });
      if (response.status === 200) {
        isAuthenticated = true; // 이 부분은 실제 인증 여부에 따라 변경해야 합니다.
        console.log(response.data);
        localStorage.setItem('sessionid', response.data);
        alert('로그인이 완료되었습니다.');
      }
      const isAdmin = response.data.isAdmin; // 관리자 여부를 응답에서 받아온다고 가정합니다.

      if (isAuthenticated) {
        if (isAdmin) {
          alert('관리자 권한 허용되었습니다.');
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