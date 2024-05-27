import React, { useState } from 'react';
import { Table, Form } from 'react-bootstrap';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import Header from '../components/Header';
import Pagination from '../components/Pagination';
import './IssueListPage.css';


function IssueListPage() {
    const { projectId } = useParams();
    const [searchParams] = useSearchParams();
    const projectName = searchParams.get('projectName');
    const navigate = useNavigate();

    const [issues, setIssues] = useState([
        { id: 1, title: 'issue#1', status: ['new'], priority: 'major', reportedDate: '2022-01-01', filter: 'filter1', assignee: 'dev1' },
        // 더미 데이터 추가
    ]);

    const [currentPage, setCurrentPage] = useState(1);
    const issuesPerPage = 10;

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const indexOfLastIssue = currentPage * issuesPerPage;
    const indexOfFirstIssue = indexOfLastIssue - issuesPerPage;
    const currentIssues = issues.slice(indexOfFirstIssue, indexOfLastIssue);

    const handleStatusChange = (issueIndex, selectedOptions) => {
        const updatedIssues = [...issues];
        updatedIssues[issueIndex].status = [...selectedOptions].map(option => option.value);
        setIssues(updatedIssues);
    };

    return (
        <div className="issue-list-container">
            <Header projectId={projectId} projectName={projectName} />
            <div className="issue-table-container">
                <h2>이슈 목록</h2>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Status</th>
                            <th>Priority</th>
                            <th>Reported Date</th>
                            <th>Filter</th>
                            <th>Assignee</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentIssues.map((issue, index) => (
                            <tr key={index}>
                                <td>{issue.title}</td>
                                <td>
                                    <Form.Control as="select" multiple value={issue.status} onChange={(e) => handleStatusChange(indexOfFirstIssue + index, e.target.selectedOptions)}>
                                        <option value="new">New</option>
                                        <option value="assigned">Assigned</option>
                                        <option value="resolved">Resolved</option>
                                        <option value="closed">Closed</option>
                                        <option value="reopened">Reopened</option>
                                    </Form.Control>
                                </td>
                                <td>
                                    <Form.Control as="select" value={issue.priority} onChange={(e) => {
                                        const updatedIssues = [...issues];
                                        updatedIssues[indexOfFirstIssue + index].priority = e.target.value;
                                        setIssues(updatedIssues);
                                    }}>
                                        <option value="blocker">Blocker</option>
                                        <option value="critical">Critical</option>
                                        <option value="major">Major</option>
                                        <option value="minor">Minor</option>
                                        <option value="trivial">Trivial</option>
                                    </Form.Control>
                                </td>
                                <td>{issue.reportedDate}</td>
                                <td>
                                    <Form.Control as="select" value={issue.filter} onChange={(e) => {
                                        const updatedIssues = [...issues];
                                        updatedIssues[indexOfFirstIssue + index].filter = e.target.value;
                                        setIssues(updatedIssues);
                                    }}>
                                        <option value="filter1">Filter1</option>
                                        <option value="filter2">Filter2</option>
                                        <option value="filter3">Filter3</option>
                                    </Form.Control>
                                </td>
                                <td>
                                    <Form.Control as="select" value={issue.assignee} onChange={(e) => {
                                        const updatedIssues = [...issues];
                                        updatedIssues[indexOfFirstIssue + index].assignee = e.target.value;
                                        setIssues(updatedIssues);
                                    }}>
                                        <option value="dev1">Dev1</option>
                                        <option value="dev2">Dev2</option>
                                        <option value="pl1">PL1</option>
                                        <option value="tester1">Tester1</option>
                                    </Form.Control>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </div>
            <Pagination totalPages={Math.ceil(issues.length / issuesPerPage)} currentPage={currentPage} onPageChange={handlePageChange} />
        </div>
    );
}

export default IssueListPage;
