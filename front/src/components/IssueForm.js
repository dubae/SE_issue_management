import React, { useState } from 'react';
import { Form, Button } from 'react-bootstrap';

const IssueForm = ({ onIssueAdded }) => {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        priority: 'major'
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const now = new Date();
        const date = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
        const time = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

        const userId = localStorage.getItem('userId');
        const author = 'pl1';

        const newIssue = {
            ...formData,
            date,
            time,
            author
        };

        console.log(newIssue);
        onIssueAdded(newIssue);
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
};

export default IssueForm;
