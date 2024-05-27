import React, { useState, useEffect } from 'react';
import { Button, Form } from 'react-bootstrap';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { useSearchParams } from 'react-router-dom';
import './IssueDetailPage.css';

function IssueDetailPage() {
    const navigate = useNavigate();
    const { projectId, issueId } = useParams();
    const [searchParams] = useSearchParams();
    const projectName = searchParams.get('projectName');
    console.log(projectId, issueId, projectName);

    // 더미 데이터로 이슈 정보 초기화
    // API 호출로 데이터를 불러오는 방식으로 변경해
    // API 호출을 완료하면 setIssue() 함수를 사용하여 상태 업데이트
    const [issue, setIssue] = useState({
        title: `issue#${issueId}`,
        description: '로그인, 로그아웃 기능 구현하기',
        reporter: 'Reporter1',
        reportedDate: '2023-05-20 17:40:11',
        assignee: ['PL1'],
        priority: 'Major',
        status: 'New',
        comments: [
            { author: 'Tester1', content: '이슈 생성했습니다.', date: '2023-05-20 17:40:00' },
            { author: 'PL1', content: '확인/assignee 지정했습니다.', date: '2023-05-22 15:10:00' }
        ]
    });

    // 이슈 정보를 불러오는 함수
    const fetchIssue = async () => {
        try {
            // 이슈 정보를 가져오는 API 호출을 여기에 추가
            // 예시: const response = await fetch(`/api/projects/${projectId}/issues/${issueId}`);
            // 예시: const data = await response.json();
            // 가져온 데이터를 상태로 설정
            // setIssue(data);
        } catch (error) {
            console.error('Error fetching issue:', error);
        }
    };


    // useEffect로 필요한 경우에 이슈 정보를 불러올 수 있음
    useEffect(() => {
        // fetchIssue();
    }, [projectId, issueId]);

    const handleLogout = () => {
        localStorage.removeItem('userId');
        navigate('/');
    };

    const handleAssigneeChange = (e) => {
        const selectedAssignees = Array.from(e.target.selectedOptions, option => option.value);
        setIssue(prevIssue => ({
            ...prevIssue,
            assignee: selectedAssignees
        }));
    };

    const handlePriorityChange = (e) => {
        setIssue(prevIssue => ({
            ...prevIssue,
            priority: e.target.value
        }));
    };

    const handleStatusChange = (e) => {
        setIssue(prevIssue => ({
            ...prevIssue,
            status: e.target.value
        }));
    };

    const renderComments = () => {
        return issue.comments.map((comment, index) => (
            <div key={index} className="comment">
                <p>{`[${comment.author}] ${comment.content} ${comment.date}`}</p>
            </div>
        ));
    };

    return (
        <div className="issue-detail-container">
            <header className="header">
                <h1>
                    <Link to="/">프로젝트</Link>/
                    <Link to={`/project/${projectId}`}>{projectId}</Link>/
                    {issue.title}
                </h1>
                <div className="buttons">
                    <Link to={`/project/${projectId}/issues?projectName=${projectName}`}><Button variant="primary">이슈 목록</Button></Link>
                    <Button variant="primary" onClick={handleLogout}>로그아웃</Button>
                </div>
            </header>
            <div className="issue-details">
                <div className="left-panel">
                    <h2>이슈 설명</h2>
                    <p>{issue.description}</p>
                    <h2>코멘트</h2>
                    {renderComments()}
                </div>
                <div className="right-panel">
                    <div className="assignee-section">
                        <h3>Assignee</h3>
                        <Form.Control as="select" multiple value={issue.assignee} onChange={handleAssigneeChange}>
                            {/* API로 사용자 정보를 불러오면 동적으로 추가해야 함 */}
                            <option value="PL1">PL1</option>
                            <option value="Dev1">Dev1</option>
                            <option value="Dev2">Dev2</option>
                            <option value="Tester2">Tester2</option>
                        </Form.Control>
                    </div>
                    <div className="priority-section">
                        <h3>Priority</h3>
                        <Form.Control as="select" value={issue.priority} onChange={handlePriorityChange}>
                            <option value="Blocker">Blocker</option>
                            <option value="Critical">Critical</option>
                            <option value="Major">Major</option>
                            <option value="Minor">Minor</option>
                            <option value="Trivial">Trivial</option>
                        </Form.Control>
                    </div>
                    <div className="status-section">
                        <h3>Status</h3>
                        <Form.Control as="select" value={issue.status} onChange={handleStatusChange}>
                            <option value="New">New</option>
                            <option value="Assigned">Assigned</option>
                            <option value="Resolved">Resolved</option>
                            <option value="Closed">Closed</option>
                            <option value="Reopened">Reopened</option>
                        </Form.Control>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default IssueDetailPage;