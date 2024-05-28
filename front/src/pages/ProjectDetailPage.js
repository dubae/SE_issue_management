import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { Link, useParams, useNavigate } from 'react-router-dom';
import './ProjectDetailPage.css';
import Modal from 'react-modal';
import IssueForm from '../components/IssueForm';
import UserInfoModal from '../components/UserInfoModal';

function ProjectDetailPage() {
    const navigate = useNavigate();
    const { projectId } = useParams();

    const [project, setProject] = useState({
        name: '프로젝트 1',
        issues: [
            { id: 1, author: 'dev1', title: 'issue#1', component: 'UI', action: 'comment update', date: '2022-01-01', time: '12:00' },
            { id: 2, author: 'pl1', title: 'issue#1', component: 'Backend', action: 'comment update', date: '2022-01-01', time: '12:00' },
            { id: 3, author: 'tester1', title: 'issue#2', component: 'Frontend', action: 'update', date: '2022-01-02', time: '13:00' }
        ],
        issueCount: 2,
        issueUpdateCount: 3
    });

    const handleLogout = () => {
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        localStorage.removeItem('name');
        navigate('/');
    };

    const handleAddIssue = (newIssue) => {
        handleToggleModal();
        setProject(prevProject => ({
            ...prevProject,
            issues: [...prevProject.issues, newIssue]
        }));
    };

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleToggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };

    const fetchProject = async () => {
        try {
            const response = await fetch(`/api/projects/${projectId}`);
            const data = await response.json();
            setProject(data);
        } catch (error) {
            console.error('Error fetching project:', error);
        }
    };

    useEffect(() => {
        fetchProject();
    }, [projectId]);

    const [showUserInfo, setShowUserInfo] = useState(false);
    const handleCloseUserInfo = () => setShowUserInfo(false);
    const handleShowUserInfo = () => setShowUserInfo(true);

    const userInfo = {
        userId: localStorage.getItem('userId') || '',
        email: localStorage.getItem('email') || '',
        name: localStorage.getItem('name') || ''
    };

    return (
        <div className="project-detail-container">
            <header className="header">
                <h1><Link to="/">프로젝트</Link>/{project.name}</h1>
                <div className="auth-buttons">
                    <Button variant="primary" onClick={handleShowUserInfo}>내 정보 보기</Button>
                    <Button variant="primary" onClick={handleLogout}>로그아웃</Button>
                </div>
            </header>
            <div className="navigation-buttons">
                <Link to={`/project/${projectId}/issues?projectName=${project.name}`}><Button variant="info">이슈 목록</Button></Link>
                <Button variant="success" onClick={handleToggleModal}>이슈 등록</Button>
                <Modal isOpen={isModalOpen} onRequestClose={handleToggleModal}>
                    <IssueForm onIssueAdded={handleAddIssue} />
                    <Button onClick={handleToggleModal}>닫기</Button>
                </Modal>
            </div>
            <div className="issue-timeline">
                <h2>이슈 타임 라인</h2>
                <ul>
                    {project.issues.map((issue, index) => (
                        <li key={index}>
                            [{issue.author}]이 <Link to={`/project/${projectId}/issues/${issue.id}?projectName=${project.name}`}>{issue.title}</Link>[{issue.component}] {issue.action} | {issue.date} | {issue.time}
                        </li>
                    ))}
                </ul>
            </div>
            <div className="issue-statistics">
                <h2>이슈 통계</h2>
                <p>이슈 생성 횟수: {project.issueCount}</p>
                <p>이슈 업데이트 횟수: {project.issueUpdateCount}</p>
            </div>

            <UserInfoModal
                show={showUserInfo}
                handleClose={handleCloseUserInfo}
                userInfo={userInfo}
            />
        </div>
    );
}

export default ProjectDetailPage;
