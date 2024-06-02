import React, { useState, useEffect } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';
const sessionid = sessionStorage.getItem('sessionid');

function IssueForm({ onIssueAdded, projectId }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    priority: 'major',
    component: '',
    status: 'New',
    fixerId: '',
    devId: '',
  });

  useEffect(() => {
    setFormData(prevData => ({
      ...prevData,
      projectId: parseInt(projectId, 10),
      writerId: null,
      userId: parseInt(sessionStorage.getItem('userId'), 10)
    }));
  }, [projectId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const issueData = {
      ...formData,
      projectId: parseInt(projectId, 10),
      writerId: sessionStorage.getItem('userId'),
    };

    console.log(issueData);

    try {
      const response = await axios.post(`${API_URL}/project/${projectId}/issue/new`, issueData, {
        headers: {
          'Content-Type': 'application/json',
          'sessionid': `${sessionStorage.getItem('sessionid')}`
        }
      });
      
      console.log('response', response)

      if (response.status === 200) {
        const createdIssue = response.data;
        onIssueAdded(createdIssue);
      } else {
        console.error('Failed to create issue:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating issue:', error);
    }
  };

  return (
    <div className="issue-form">
      <h2>Issue 생성</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formIssueTitle">
          <Form.Label>Title *</Form.Label>
          <Form.Control
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
          />
        </Form.Group>
        <Form.Group controlId="formIssueDescription">
          <Form.Label>Description *</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            name="description"
            value={formData.description}
            onChange={handleChange}
            required
          />
        </Form.Group>
        <Form.Group controlId="formIssueComponent">
          <Form.Label>Component</Form.Label>
          <Form.Control
            type="text"
            name="component"
            value={formData.component}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group controlId="formIssuePriority">
          <Form.Label>Priority</Form.Label>
          <Form.Control
            as="select"
            name="priority"
            value={formData.priority}
            onChange={handleChange}
          >
            <option value="blocker">Blocker</option>
            <option value="critical">Critical</option>
            <option value="major">Major</option>
            <option value="minor">Minor</option>
            <option value="trivial">Trivial</option>
          </Form.Control>
        </Form.Group>
        <Button variant="primary" type="submit">
          생성
        </Button>
      </Form>
    </div>
  );
}

export default IssueForm;
