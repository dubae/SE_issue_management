import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './MainPage.css';
import UserInfoModal from '../components/UserInfoModal';
import axios from 'axios';

function MainPage() {
    const [loggedIn, setLoggedIn] = useState(localStorage.getItem('userId') ? true : false);
    const [isAdmin, setIsAdmin] = useState(localStorage.getItem('isAdmin') === 'true');
    const [userInfo, setUserInfo] = useState({
        userId: localStorage.getItem('userId') || '',
        email: localStorage.getItem('email') || '',
        name: localStorage.getItem('name') || ''
    });
    const [showUserInfo, setShowUserInfo] = useState(false);
    const handleCloseUserInfo = () => setShowUserInfo(false);
    const handleShowUserInfo = () => setShowUserInfo(true);
    useEffect(() => {
        // 페이지가 로드될 때 실행되는 GET 요청 코드
        fetchProjects();
    }, []);
    

    const fetchProjects = async () => {
        try {
            let sessionid = localStorage.getItem('sessionid');
            const response = await axios.post('http://localhost:8080/api/projects', {}, {
                headers: {
                  'sessionid': `${sessionid}`
                },
                withCredentials: true
              });
            console.log(response);
            const data = response.data;
            setProjects(data);
        } catch (error) {
            console.error('프로젝트를 가져오는 중 에러 발생:', error);
        }
    };

    useEffect(() => {
        const handleStorageChange = () => {
            setLoggedIn(localStorage.getItem('userId') ? true : false);
            setIsAdmin(localStorage.getItem('isAdmin') === 'true');
            setUserInfo({
                userId: localStorage.getItem('userId') || '',
                email: localStorage.getItem('email') || '',
                name: localStorage.getItem('name') || ''
            });
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    const [projects, setProjects] = useState([]);

    const [show, setShow] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);
    const [selectedIndex, setSelectedIndex] = useState(null);
    const [editingProject, setEditingProject] = useState(null);
    const [newProject, setNewProject] = useState({
        name: '',
        plAccount: '',
        testerAccount: '',
        devAccount: '',
        description: ''
    });

    const handleClose = () => setShow(false);
    const handleShow = () => {
        setEditingProject(null);
        setNewProject({
            name: '',
            plAccount: '',
            testerAccount: '',
            devAccount: '',
            description: ''
        });
        setShow(true);
    };
    const handleCloseConfirm = () => setShowConfirm(false);
    const handleShowConfirm = (index) => {
        setSelectedIndex(index);
        setShowConfirm(true);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNewProject({ ...newProject, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!newProject.name || !newProject.plAccount || !newProject.testerAccount || !newProject.devAccount) {
            alert("필수 입력란을 모두 채워주세요.");
            return;
        }

        if (editingProject) {
            const updatedProjects = projects.map((project) =>
                project.id === editingProject.id ? { ...editingProject, ...newProject } : project
            );
            setProjects(updatedProjects);
        } else {
            const projectWithDate = {
                id: projects.length + 1,
                ...newProject,
                createdAt: new Date().toISOString().split('T')[0],
                status: '진행중'
            };
            setProjects([...projects, projectWithDate]);
        }

        setNewProject({
            name: '',
            plAccount: '',
            testerAccount: '',
            devAccount: '',
            description: ''
        });
        handleClose();
    };

    const handleDelete = () => {
        const newProjects = [...projects];
        newProjects.splice(selectedIndex, 1);
        setProjects(newProjects);
        handleCloseConfirm();
    };

    const handleLogout = () => {
        setLoggedIn(false);
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        localStorage.removeItem('name');
        localStorage.removeItem('isAdmin');
    };

    const handleEdit = (index) => {
        setEditingProject(projects[index]);
        setNewProject(projects[index]);
        setShow(true);
    };

    return (
        <div className="main-container">
            <header className="header">
                <h1>프로젝트</h1>
                <div className="auth-buttons">
                    {loggedIn ? (
                        <>
                            <Button variant="primary" onClick={handleShowUserInfo}>내 정보 보기</Button>
                            <Button variant="primary" onClick={handleLogout}>로그아웃</Button>
                        </>
                    ) : (
                        <>
                            <Link to="/login"><Button variant="primary">로그인</Button></Link>
                            <Link to="/signup"><Button variant="secondary">회원가입</Button></Link>
                        </>
                    )}
                </div>
            </header>
            {loggedIn && (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>프로젝트 이름</th>
                            <th>개요</th>
                            <th>프로젝트 생성날짜</th>
                            <th>상태</th>
                            {isAdmin && <th>수정</th>}
                            <th>삭제</th>
                        </tr>
                    </thead>
                    <tbody>
                        {projects.map((project, index) => (
                            <tr key={index}>
                                <td>
                                    <Link
                                        to={{
                                            pathname: `/project/${project.id}`,
                                            state: { projectData: project }
                                        }}
                                    >
                                        {project.name}
                                    </Link>
                                </td>
                                <td>{project.description}</td>
                                <td>{project.createdAt}</td>
                                <td>{project.status}</td>
                                {isAdmin && (
                                    <td>
                                        <Button variant="warning" onClick={() => handleEdit(index)}>수정</Button>
                                    </td>
                                )}
                                <td>
                                    <Button variant="danger" onClick={() => handleShowConfirm(index)}>삭제</Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
            {loggedIn && (
                <div className="create-button">
                    <Button variant="success" onClick={handleShow}>프로젝트 생성</Button>
                </div>
            )}

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingProject ? '프로젝트 수정' : '새 프로젝트 추가'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formProjectName">
                            <Form.Label>프로젝트 이름 *</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={newProject.name}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formPLAccount">
                            <Form.Label>PL 계정 *</Form.Label>
                            <Form.Control
                                type="text"
                                name="plAccount"
                                value={newProject.plAccount}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formTesterAccount">
                            <Form.Label>Tester 계정 *</Form.Label>
                            <Form.Control
                                type="text"
                                name="testerAccount"
                                value={newProject.testerAccount}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formDevAccount">
                            <Form.Label>Dev 계정 *</Form.Label>
                            <Form.Control
                                type="text"
                                name="devAccount"
                                value={newProject.devAccount}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formProjectDescription">
                            <Form.Label>프로젝트 개요</Form.Label>
                            <Form.Control
                                type="text"
                                name="description"
                                value={newProject.description}
                                onChange={handleChange}
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            저장
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
            <Modal show={showConfirm} onHide={handleCloseConfirm}>
                <Modal.Header closeButton>
                    <Modal.Title>프로젝트 삭제</Modal.Title>
                </Modal.Header>
                <Modal.Body>정말로 이 프로젝트를 삭제하시겠습니까?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseConfirm}>
                        취소
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        삭제
                    </Button>
                </Modal.Footer>
            </Modal>

            <UserInfoModal
                show={showUserInfo}
                handleClose={handleCloseUserInfo}
                userInfo={userInfo}
            />
        </div>
    );
}

export default MainPage;
