import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from 'react-bootstrap';

const Header = ({ projectId, projectName, issueTitle }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('userId');
        navigate('/');
    };

    return (
        <header className="header">
            <h1>
                <Link to="/">프로젝트</Link>/
                <Link to={`/project/${projectId}`}>{projectName}</Link>/
                {issueTitle && <span>{issueTitle}</span>}
            </h1>
            <div className="buttons">
                <Button variant="primary" onClick={handleLogout}>로그아웃</Button>
            </div>
        </header>
    );
};

export default Header;
