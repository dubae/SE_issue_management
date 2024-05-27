import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useSearchParams } from 'react-router-dom';
import Header from '../components/Header';
import CommentSection from '../components/CommentSection';
import Dropdown from '../components/Dropdown';
import './IssueDetailPage.css';


function IssueDetailPage() {
    const navigate = useNavigate();
    const { projectId, issueId } = useParams();
    const [searchParams] = useSearchParams();
    const projectName = searchParams.get('projectName');

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

    const fetchIssue = async () => {
        try {
            // API 호출을 여기에 추가
        } catch (error) {
            console.error('Error fetching issue:', error);
        }
    };

    useEffect(() => {
        // fetchIssue();
    }, [projectId, issueId]);

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

    return (
        <div className="issue-detail-container">
            <Header projectId={projectId} projectName={projectName} issueTitle={issue.title} />
            <div className="issue-details">
                <div className="left-panel">
                    <h2>이슈 설명</h2>
                    <p>{issue.description}</p>
                    <CommentSection comments={issue.comments} />
                </div>
                <div className="right-panel">
                    <Dropdown
                        label="Assignee"
                        options={[
                            { value: 'PL1', label: 'PL1' },
                            { value: 'Dev1', label: 'Dev1' },
                            { value: 'Dev2', label: 'Dev2' },
                            { value: 'Tester2', label: 'Tester2' }
                        ]}
                        value={issue.assignee}
                        onChange={handleAssigneeChange}
                        multiple
                    />
                    <Dropdown
                        label="Priority"
                        options={[
                            { value: 'Blocker', label: 'Blocker' },
                            { value: 'Critical', label: 'Critical' },
                            { value: 'Major', label: 'Major' },
                            { value: 'Minor', label: 'Minor' },
                            { value: 'Trivial', label: 'Trivial' }
                        ]}
                        value={issue.priority}
                        onChange={handlePriorityChange}
                    />
                    <Dropdown
                        label="Status"
                        options={[
                            { value: 'New', label: 'New' },
                            { value: 'Assigned', label: 'Assigned' },
                            { value: 'Resolved', label: 'Resolved' },
                            { value: 'Closed', label: 'Closed' },
                            { value: 'Reopened', label: 'Reopened' }
                        ]}
                        value={issue.status}
                        onChange={handleStatusChange}
                    />
                </div>
            </div>
        </div>
    );
}

export default IssueDetailPage;
