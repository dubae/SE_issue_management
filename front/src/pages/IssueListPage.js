import React, { useState } from 'react';
import { Table, Form } from 'react-bootstrap';
import { useParams, useSearchParams, Link } from 'react-router-dom';
import Header from '../components/Header';
import Pagination from '../components/Pagination';
import './IssueListPage.css';

function IssueListPage() {
  const { projectId } = useParams();
  const [searchParams] = useSearchParams();
  const projectName = searchParams.get('projectName');

  const [searchOption, setSearchOption] = useState('');
  const [searchText, setSearchText] = useState('');
  const [priorityFixer, setPriorityFixer] = useState('');
  const [statusFixer, setStatusFixer] = useState('');

  const handleSearchOptionChange = (e) => {
    setSearchOption(e.target.value);
  };

  const handleSearchTextChange = (e) => {
    setSearchText(e.target.value);
  };

  const handlePriorityFixerChange = (e) => {
    setPriorityFixer(e.target.value);
  };

  const handleStatusFixerChange = (e) => {
    setStatusFixer(e.target.value);
  };

  const [issues, setIssues] = useState([
    { id: 1, component: 'component#1', title: 'issue#1', status: ['new'], priority: 'major', reportedDate: '2022-01-01', fixer: 'fixer1', assignee: 'dev1' },
    { id: 2, component: 'component#2', title: 'issue#2', status: ['assigned'], priority: 'critical', reportedDate: '2022-01-02', fixer: 'fixer2', assignee: 'tester1' },
    { id: 3, component: 'component#1', title: 'issue#3', status: ['new'], priority: 'major', reportedDate: '2022-01-03', fixer: 'fixer1', assignee: 'dev1' },
    { id: 4, component: 'component#1', title: 'issue#4', status: ['new'], priority: 'minor', reportedDate: '2022-01-04', fixer: 'fixer1', assignee: 'dev1' },
    { id: 5, component: 'component#2', title: 'issue#5', status: ['assigned'], priority: 'blocker', reportedDate: '2022-01-05', fixer: 'fixer2', assignee: 'tester1' },
    { id: 6, component: 'component#2', title: 'issue#6', status: ['resolved'], priority: 'critical', reportedDate: '2022-01-06', fixer: 'fixer2', assignee: 'tester1' },
    { id: 7, component: 'component#3', title: 'issue#7', status: ['reopened'], priority: 'trivial', reportedDate: '2022-01-07', fixer: 'fixer3', assignee: 'pl1' },
    { id: 8, component: 'component#3', title: 'issue#8', status: ['closed'], priority: 'major', reportedDate: '2022-01-08', fixer: 'fixer3', assignee: 'pl1' },
    { id: 9, component: 'component#3', title: 'issue#9', status: ['new'], priority: 'minor', reportedDate: '2022-01-09', fixer: 'fixer3', assignee: 'pl1' },
    { id: 10, component: 'component#1', title: 'issue#10', status: ['new'], priority: 'critical', reportedDate: '2022-01-10', fixer: 'fixer1', assignee: 'dev1' },
    { id: 11, component: 'component#2', title: 'issue#11', status: ['assigned'], priority: 'major', reportedDate: '2022-01-11', fixer: 'fixer2', assignee: 'tester1' }
  ]);

  const [currentPage, setCurrentPage] = useState(1);
  const issuesPerPage = 10;

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const indexOfLastIssue = currentPage * issuesPerPage;
  const indexOfFirstIssue = indexOfLastIssue - issuesPerPage;
  const currentIssues = issues.slice(indexOfFirstIssue, indexOfLastIssue);

  const fixedIssues = issues.filter(issue => {
    let matchesSearchText = true;
    let matchesPriority = true;
    let matchesStatus = true;

    if (searchText) {
      switch (searchOption) {
        case 'id':
          matchesSearchText = issue.id.toString().includes(searchText);
          break;
        case 'title':
          matchesSearchText = issue.title.toLowerCase().includes(searchText.toLowerCase());
          break;
        case 'status':
          matchesSearchText = issue.status.some(status => status.toLowerCase().includes(searchText.toLowerCase()));
          break;
        default:
          matchesSearchText = true;
      }
    }

    if (priorityFixer) {
      matchesPriority = issue.priority.toLowerCase() === priorityFixer.toLowerCase();
    }

    if (statusFixer) {
      matchesStatus = issue.status.includes(statusFixer.toLowerCase());
    }

    return matchesSearchText && matchesPriority && matchesStatus;
  });

  const displayedIssues = fixedIssues.slice(indexOfFirstIssue, indexOfLastIssue);

  return (
    <div className="issue-list-container">
      <Header projectId={projectId} projectName={projectName} />
      <div className="search-container">
        <Form.Group controlId="searchForm">
          <Form.Label>검색:</Form.Label>
          <Form.Control as="select" onChange={handleSearchOptionChange}>
            <option value="">선택하세요</option>
            <option value="id">ID</option>
            <option value="title">Title</option>
          </Form.Control>
          <Form.Control
            type="text"
            placeholder="search...."
            value={searchText}
            onChange={handleSearchTextChange}
          />
        </Form.Group>
        <Form.Group controlId="priorityFixer">
          <Form.Label>Priority:</Form.Label>
          <Form.Control as="select" onChange={handlePriorityFixerChange}>
            <option value="">전체</option>
            <option value="blocker">Blocker</option>
            <option value="critical">Critical</option>
            <option value="major">Major</option>
            <option value="minor">Minor</option>
            <option value="trivial">Trivial</option>
          </Form.Control>
        </Form.Group>
        <Form.Group controlId="statusFixer">
          <Form.Label>Status:</Form.Label>
          <Form.Control as="select" onChange={handleStatusFixerChange}>
            <option value="">전체</option>
            <option value="new">New</option>
            <option value="assigned">Assigned</option>
            <option value="resolved">Resolved</option>
            <option value="closed">Closed</option>
            <option value="reopened">Reopened</option>
          </Form.Control>
        </Form.Group>
      </div>
      <div className="issue-table-container">
        <h2>이슈 목록</h2>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Component</th>
              <th>Title</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Reported Date</th>
              <th>Fixer</th>
              <th>Assignee</th>
            </tr>
          </thead>
          <tbody>
            {displayedIssues.map((issue) => (
              <tr key={issue.id}>
                <td>{issue.component}</td>
                <td>
                  <Link to={`/project/${projectId}/issues/${issue.id}?projectName=${projectName}`}>
                    {issue.title}
                  </Link>
                </td>
                <td>{issue.status.join(', ')}</td>
                <td>{issue.priority}</td>
                <td>{issue.reportedDate}</td>
                <td>{issue.fixer}</td>
                <td>{issue.assignee}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      <Pagination
        currentPage={currentPage}
        totalItems={fixedIssues.length}
        itemsPerPage={issuesPerPage}
        onPageChange={handlePageChange}
      />
    </div>
  );
}

export default IssueListPage;
