import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { Link, useParams, useNavigate } from 'react-router-dom';
import './ProjectDetailPage.css';
import axios from 'axios';
import Modal from 'react-modal';
import IssueForm from '../pages/IssueForm';
import UserInfoModal from '../components/UserInfoModal';

const API_URL = 'http://localhost:8080/api';

function ProjectDetailPage() {
    const navigate = useNavigate();
    const { projectId } = useParams();

    const [project, setProject] = useState({ name: '' });
    const [allIssues, setAllIssues] = useState([]);
    const [filteredIssues, setFilteredIssues] = useState([]);
    const [timeFrame, setTimeFrame] = useState('month');

    const handleLogout = () => {
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        localStorage.removeItem('name');
        navigate('/');
    };

    const handleAddIssue = (newIssue) => {
        handleToggleModal();
        setAllIssues(prevIssues => [...prevIssues, newIssue]);
    };

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleToggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };



    const fetchProject = async () => {
        try {
            const userId = localStorage.getItem('userId');
            const response = await axios.get(`${API_URL}/projects`, {
                headers: {
                    'userId': userId
                }
            });
            const projects = response.data;
            const selectedProject = projects.find(proj => proj.projectid.toString() === projectId);
            if (selectedProject) {
                setProject({
                    name: selectedProject.projectname,
                });
            } else {
                console.error('Project not found');
            }
        } catch (error) {
            console.error('Error fetching projects:', error);
        }
    };

    const fetchIssues = async () => {
        try {
            const response = await axios.get(`${API_URL}/issues/${projectId}`);
            const data = response.data;
            setAllIssues(data);
            filterIssues(data, timeFrame);
        } catch (error) {
            console.error('Error fetching issues:', error);
        }
    };

    useEffect(() => {
        fetchProject();
        fetchIssues();
    }, [projectId]);

    useEffect(() => {
        filterIssues(allIssues, timeFrame);
    }, [timeFrame, allIssues]);

    const filterIssues = (issues, timeFrame) => {
        const now = new Date();
        let filtered = [];

        switch (timeFrame) {
            case 'month':
                filtered = issues.filter(issue => new Date(issue.createdAt) >= new Date(now.getFullYear(), now.getMonth(), 1));
                break;
            case 'week':
                const startOfWeek = new Date(now.setDate(now.getDate() - now.getDay()));
                filtered = issues.filter(issue => new Date(issue.createdAt) >= startOfWeek);
                break;
            case 'day':
                filtered = issues.filter(issue => new Date(issue.createdAt).toDateString() === now.toDateString());
                break;
            default:
                filtered = issues;
                break;
        }

        setFilteredIssues(filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)));
    };

    const [showUserInfo, setShowUserInfo] = useState(false);
    const handleCloseUserInfo = () => setShowUserInfo(false);
    const handleShowUserInfo = () => setShowUserInfo(true);

    const userInfo = {
        userId: localStorage.getItem('userId') || '',
        email: localStorage.getItem('email') || '',
        name: localStorage.getItem('name') || ''
    };

    const calculateIssueUpdateCount = (issues) => {
        return issues.reduce((count, issue) => count + (issue.modifyCount || 0), 0);
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
                    <IssueForm onIssueAdded={handleAddIssue} projectId={projectId} />
                    <Button onClick={handleToggleModal}>닫기</Button>
                </Modal>
            </div>
            <div className="filter-buttons">
                <Button variant={timeFrame === 'month' ? 'primary' : 'secondary'} onClick={() => setTimeFrame('month')}>월별</Button>
                <Button variant={timeFrame === 'week' ? 'primary' : 'secondary'} onClick={() => setTimeFrame('week')}>주별</Button>
                <Button variant={timeFrame === 'day' ? 'primary' : 'secondary'} onClick={() => setTimeFrame('day')}>일별</Button>
            </div>
            <div className="issue-timeline">
                <h2>이슈 타임 라인</h2>
                <ul>
                    {filteredIssues.map((issue, index) => (
                        <li key={index}>
                            [{issue.writerId}]이 <Link to={`/project/${projectId}/issues/${issue.id}?projectName=${project.name}`}>{issue.title}</Link>[{issue.component}] {'Update'} | {new Date(issue.createdAt).toLocaleDateString()} | {new Date(issue.createdAt).toLocaleTimeString()}
                        </li>
                    ))}
                </ul>
            </div>
            <div className="issue-statistics">
                <h2>이슈 통계</h2>
                <p>이슈 생성 횟수: {allIssues.length}</p>
                <p>이슈 업데이트 횟수: {calculateIssueUpdateCount(allIssues)}</p>
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
