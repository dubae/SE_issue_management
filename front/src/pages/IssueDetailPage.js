import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import Header from '../components/Header';
import CommentSection from '../components/CommentSection';
import Dropdown from '../components/Dropdown2';
import UserInfoModal from '../components/UserInfoModal';
import './IssueDetailPage.css';

const API_URL = 'http://localhost:8080/api';

function IssueDetailPage() {
    const navigate = useNavigate();
    const { projectId, issueId } = useParams();
    const [searchParams] = useSearchParams();
    const projectName = searchParams.get('projectName');

    const [issue, setIssue] = useState({
        id: null,
        writerId: '',
        projectId: null,
        devId: '',
        fixerId: '',
        title: '',
        status: '',
        component: '',
        priority: '',
        description: '',
        modifyCount: 0,
        createdAt: '',
        comments: []
    });

    const [members, setMembers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [showUserModal, setShowUserModal] = useState(false);
    const [updateData, setUpdateData] = useState({
        assignee: '',
        priority: '',
        status: ''
    });

    const fetchIssue = async () => {
        try {
            const response = await axios.get(`${API_URL}/issue/detail/${issueId}`); //경로
            setIssue(response.data);
            setUpdateData({
                assignee: response.data.devId,
                priority: response.data.priority,
                status: response.data.status
            });
        } catch (error) {
            console.error('Error fetching issue:', error);
        }
    };

    const fetchMembers = async () => {
        try {
            const response = await axios.get(`${API_URL}/projects/${projectId}/members`);
            setMembers(response.data);
        } catch (error) {
            console.error('Error fetching members:', error);
        }
    };

    useEffect(() => {
        fetchIssue();
        fetchMembers();
    }, [projectId, issueId]);

    const handleChange = async (e) => {
        const { name, value } = e.target;
        console.log('Name:', name);
        console.log('Value:', value);
        const newUpdateData = { ...updateData, [name]: value };

        try {
            await axios.post(`${API_URL}/issue/${issueId}/info`, newUpdateData);
            setUpdateData(newUpdateData);
            setIssue(prevIssue => ({ ...prevIssue, ...newUpdateData }));
        } catch (error) {
            console.error('Error updating issue info:', error);
        }
    };

    const handleUserClick = (userId) => {
        const user = members.find(member => member.userid === userId);
        if (user) {
            setSelectedUser(user);
            setShowUserModal(true);
        }
    };

    const handleCloseUserModal = () => {
        setShowUserModal(false);
        setSelectedUser(null);
    };

    return (
        <div className="issue-detail-container">
            <Header projectId={projectId} projectName={projectName} issueTitle={issue.title} />
            <div className="issue-details">
                <div className="left-panel">
                    <h2>이슈 설명</h2>
                    <p>{issue.description}</p>
                    <CommentSection comments={issue.comments || []} onUserClick={handleUserClick} />
                </div>
                <div className="right-panel">
                    <p>Component: {issue.component}</p>
                    <p>Reported Date : {issue.createdAt}</p>
                    <Dropdown
                        label="Assignee"
                        name="assignee"
                        options={members.map(member => ({ value: member.userid, label: member.username }))}
                        value={updateData.assignee}
                        onChange={handleChange}
                    />
                    <Dropdown
                        label="Priority"
                        name="priority"
                        options={[
                            { value: 'Blocker', label: 'Blocker' },
                            { value: 'Critical', label: 'Critical' },
                            { value: 'Major', label: 'Major' },
                            { value: 'Minor', label: 'Minor' },
                            { value: 'Trivial', label: 'Trivial' }
                        ]}
                        value={updateData.priority}
                        onChange={handleChange}
                    />
                    <Dropdown
                        label="Status"
                        name="status"
                        options={[
                            { value: 'New', label: 'New' },
                            { value: 'Assigned', label: 'Assigned' },
                            { value: 'Resolved', label: 'Resolved' },
                            { value: 'Closed', label: 'Closed' },
                            { value: 'Reopened', label: 'Reopened' }
                        ]}
                        value={updateData.status}
                        onChange={handleChange}
                    />
                </div>
            </div>
            {selectedUser && (
                <UserInfoModal
                    show={showUserModal}
                    handleClose={handleCloseUserModal}
                    userInfo={selectedUser}
                />
            )}
        </div>
    );
}

export default IssueDetailPage;
