import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './MainPage.css';
import UserInfoModal from '../components/UserInfoModal';

const API_URL = 'http://localhost:8080/api';

function MainPage() {
    const [loggedIn, setLoggedIn] = useState(localStorage.getItem('userId') ? true : false);
    const [userInfo, setUserInfo] = useState({
        userId: localStorage.getItem('userId') || '',
        email: '',
        name: ''
    });
    const [showUserInfo, setShowUserInfo] = useState(false);

    const handleCloseUserInfo = () => setShowUserInfo(false);
    const handleShowUserInfo = async () => {
        try {
            const response = await fetch(`${API_URL}/member/${localStorage.getItem('userId')}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'sessionid': localStorage.getItem('sessionid') // 세션 ID를 헤더에 포함
                }
            });
    
            if (!response.ok) {
                let errorMsg = `HTTP error! Status: ${response.status}`;
                if (response.status === 401) {
                    errorMsg = 'Unauthorized access. Please log in again.';
                } else if (response.status === 404) {
                    errorMsg = 'User not found.';
                }
                throw new Error(errorMsg);
            }
    
            const data = await response.json();
            
            if (!data || !data.userid || !data.email || !data.username) {
                throw new Error('Incomplete response data');
            }
    
            setUserInfo({
                userId: data.userid,
                email: data.email,
                name: data.username
            });
    
            setShowUserInfo(true);
        } catch (error) {
            console.error('Error fetching user info:', error.message);
        }
    };
    

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

    useEffect(() => {
        const handleStorageChange = () => {
            setLoggedIn(localStorage.getItem('userId') ? true : false);
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);
/*
    useEffect(() => {
        const fetchProjects = async () => {
            const response = await fetch(`${API_URL}/projects`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'userId': localStorage.getItem('userId')
                }
            });

            const data = await response.json();
            console.log(data);
            const transformedProjects = data.map((project) => ({
               id: project.projectid,
               name: project.projectname,
               description: project.projectdescription,
               createdAt: project.projectcreatedtime,
               status: project.projectstatus,
               plAccount: project.members.find(member => member.userRole === 'PL')?.userid || '',
               testerAccount: project.members.find(member => member.userRole === 'Tester')?.userid || '',
               devAccount: project.members.find(member => member.userRole === 'Dev')?.userid || ''
            }));

            setProjects(transformedProjects);
        };

        fetchProjects();
    }, []);
*/
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!newProject.name || !newProject.plAccount || !newProject.testerAccount || !newProject.devAccount) {
            alert("필수 입력란을 모두 채워주세요.");
            return;
        }

        const projectData = {
            projectid: editingProject ? editingProject.id : projects.length + 1,
            projectname: newProject.name,
            projectdescription: newProject.description,
            projectcreatedAt: editingProject ? editingProject.createdAt : new Date().toISOString().split('T')[0],
            projectstatus: editingProject ? editingProject.status : '진행중'
        };

        const addProjectDTO = {
            projectDTO: projectData,
            pl: newProject.plAccount,
            dev: newProject.devAccount,
            tester: newProject.testerAccount,
            pm: userInfo.userId
        };

        const response = await fetch(`${API_URL}/addproject`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'userId': localStorage.getItem('userId')
            },
            body: JSON.stringify(addProjectDTO),
        });

        if (response.ok) {
            const updatedProjects = editingProject
                ? projects.map((project) =>
                      project.id === editingProject.id ? projectData : project
                  )
                : [...projects, projectData];
            setProjects(updatedProjects);
            setNewProject({
                name: '',
                plAccount: '',
                testerAccount: '',
                devAccount: '',
                description: ''
            });
            handleClose();
        } else {
            alert('프로젝트를 저장하는데 실패했습니다.');
        }
    };

    const handleDelete = async () => {
        const projectToDelete = projects[selectedIndex];

        const response = await fetch(`${API_URL}/addproject/${projectToDelete.name}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'userId': localStorage.getItem('userId')
            }
        });

        if (response.ok) {
            const newProjects = projects.filter((_, index) => index !== selectedIndex);
            setProjects(newProjects);
            handleCloseConfirm();
        } else {
            alert('프로젝트 삭제에 실패했습니다.');
        }
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
        setNewProject({
            name: projects[index].name,
            plAccount: projects[index].plAccount,
            testerAccount: projects[index].testerAccount,
            devAccount: projects[index].devAccount,
            description: projects[index].description
        });
        setShow(true);
    };

    const handleStatusChange = async (projectName, newStatus, currentStatus) => {
        console.log(newStatus);
        const response = await fetch(`${API_URL}/updateproject/${projectName}/${newStatus}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'userId': localStorage.getItem('userId')
            }
        });

        if (response.ok) {
            setProjects(projects.map(project =>
                project.name === projectName ? { ...project, status: newStatus } : project
            ));
        } else {
            alert('상태를 변경하는데 실패했습니다.');
            setProjects(projects.map(project =>
                project.name === projectName ? { ...project, status: currentStatus } : project
            ));
        }
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
                            {localStorage.getItem('isAdmin') === 'true' && <th>수정</th>}
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
                                <td>
                                    <Form.Control
                                        as="select"
                                        value={project.status}
                                        onChange={(e) => handleStatusChange(project.name, e.target.value, project.status)}
                                    >
                                        <option value="진행중">진행중</option>
                                        <option value="완료">완료</option>
                                        <option value="보류">보류</option>
                                    </Form.Control>
                                </td>
                                {localStorage.getItem('isAdmin') === 'true' && (
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
