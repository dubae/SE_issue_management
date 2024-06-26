import React, { useState, useEffect } from 'react';
import { Table, Form } from 'react-bootstrap';
import { useParams, useSearchParams, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from '../components/Header';
import Pagination from '../components/Pagination';
import './IssueListPage.css';

const API_URL = 'http://localhost:8080/api';

function IssueListPage() {
  const navigate = useNavigate();
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

  const [issues, setIssues] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const issuesPerPage = 10;
  
  useEffect(() => {
    (async () => {
      try {
        const result = await fetch(API_URL+'/login_status', {
          method:'POST',
          headers: {
            userid: sessionStorage.getItem('userid'),
            sessionid: sessionStorage.getItem('sessionid')
          },
        });
        if (!result?.ok) navigate("/");
        console.log('result', result, await result.text())
      } catch (err) {
        navigate("/");
      }
    })();
  }, [navigate]);

  useEffect(() => {
    axios.get(`${API_URL}/issues/${projectId}`, {
      headers: {
        'sessionid': sessionStorage.getItem("sessionid")
      }
    })
      .then(response => {
        setIssues(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the issues!', error);
      });
  }, [projectId]);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const indexOfLastIssue = currentPage * issuesPerPage;
  const indexOfFirstIssue = indexOfLastIssue - issuesPerPage;
  const currentIssues = issues.slice(indexOfFirstIssue, indexOfLastIssue);

  const filteredIssues = issues.filter(issue => {
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
          matchesSearchText = issue.status.toLowerCase().includes(searchText.toLowerCase());
          break;
        default:
          matchesSearchText = true;
      }
    }

    if (priorityFixer) {
      matchesPriority = issue.priority.toLowerCase() === priorityFixer.toLowerCase();
    }

    if (statusFixer) {
      matchesStatus = issue.status.toLowerCase() === statusFixer.toLowerCase();
    }

    return matchesSearchText && matchesPriority && matchesStatus;
  });

  const displayedIssues = filteredIssues.slice(indexOfFirstIssue, indexOfLastIssue);

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
            <option value="Blocker">Blocker</option>
            <option value="Critical">Critical</option>
            <option value="Major">Major</option>
            <option value="Minor">Minor</option>
            <option value="Trivial">Trivial</option>
          </Form.Control>
        </Form.Group>
        <Form.Group controlId="statusFixer">
          <Form.Label>Status:</Form.Label>
          <Form.Control as="select" onChange={handleStatusFixerChange}>
            <option value="">전체</option>
            <option value="New">New</option>
            <option value="Assigned">Assigned</option>
            <option value="Resolved">Resolved</option>
            <option value="Closed">Closed</option>
            <option value="Reopened">Reopened</option>
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
                <td>{issue.status}</td>
                <td>{issue.priority}</td>
                <td>{issue.createdAt}</td>
                <td>{issue.status === 'Closed' ? issue.devId : issue.fixerId}</td>
                <td>{issue.devId}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      <Pagination
        currentPage={currentPage}
        totalItems={filteredIssues.length}
        itemsPerPage={issuesPerPage}
        onPageChange={handlePageChange}
      />
    </div>
  );
}

export default IssueListPage;
