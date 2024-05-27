import React, { useState } from 'react';
import { Button, Table, Form } from 'react-bootstrap';
import { Link, useParams, useNavigate, useSearchParams } from 'react-router-dom';
import './IssueListPage.css';

function IssueListPage() {
  const { projectId } = useParams();
  const [searchParams] = useSearchParams();
  const projectName = searchParams.get('projectName');
  const navigate = useNavigate();

  // 이슈 목록 불러오는 API 호출로 대체.
  const [issues, setIssues] = useState([
    { id: 1, title: 'issue#1', status: ['new'], priority: 'major', reportedDate: '2022-01-01', filter: 'filter1', assignee: 'dev1' },
    { id: 2, title: 'issue#2', status: ['assigned'], priority: 'critical', reportedDate: '2022-01-02', filter: 'filter2', assignee: 'tester1' },
    { id: 3, title: 'issue#3', status: ['new'], priority: 'major', reportedDate: '2022-01-03', filter: 'filter1', assignee: 'dev1' },
    { id: 4, title: 'issue#4', status: ['new'], priority: 'minor', reportedDate: '2022-01-04', filter: 'filter1', assignee: 'dev1' },
    { id: 5, title: 'issue#5', status: ['assigned'], priority: 'blocker', reportedDate: '2022-01-05', filter: 'filter2', assignee: 'tester1' },
    { id: 6, title: 'issue#6', status: ['resolved'], priority: 'critical', reportedDate: '2022-01-06', filter: 'filter2', assignee: 'tester1' },
    { id: 7, title: 'issue#7', status: ['reopened'], priority: 'trivial', reportedDate: '2022-01-07', filter: 'filter3', assignee: 'pl1' },
    { id: 8, title: 'issue#8', status: ['closed'], priority: 'major', reportedDate: '2022-01-08', filter: 'filter3', assignee: 'pl1' },
    { id: 9, title: 'issue#9', status: ['new'], priority: 'minor', reportedDate: '2022-01-09', filter: 'filter3', assignee: 'pl1' },
    { id: 10, title: 'issue#10', status: ['new'], priority: 'critical', reportedDate: '2022-01-10', filter: 'filter1', assignee: 'dev1' },
    { id: 11, title: 'issue#11', status: ['assigned'], priority: 'major', reportedDate: '2022-01-11', filter: 'filter2', assignee: 'tester1' }
  ]);

  const [currentPage, setCurrentPage] = useState(1);
  const issuesPerPage = 10;

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const indexOfLastIssue = currentPage * issuesPerPage;
  const indexOfFirstIssue = indexOfLastIssue - issuesPerPage;
  const currentIssues = issues.slice(indexOfFirstIssue, indexOfLastIssue);

  const renderPageNumbers = () => {
    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(issues.length / issuesPerPage); i++) {
      pageNumbers.push(i);
    }
    return pageNumbers.map(number => (
      <Button key={number} onClick={() => handlePageChange(number)}>{number}</Button>
    ));
  };

  const handleStatusChange = (issueIndex, selectedOptions) => {
    const updatedIssues = [...issues];
    updatedIssues[issueIndex].status = [...selectedOptions].map(option => option.value);
    setIssues(updatedIssues);
  };

  return (
    <div className="issue-list-container">
      <header className="header">
        <h1>
          <Link to="/">프로젝트</Link>/
          <Link to={`/project/${projectId}`}>{projectName}</Link>
        </h1>
        <div className="logout-button">
          <Button variant="primary" onClick={() => navigate('/')}>로그아웃</Button>
        </div>
      </header>
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
      <div className="pagination">
        {renderPageNumbers()}
      </div>
    </div>
  );
}

export default IssueListPage;
