import { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './MainPage.css';

function MainPage() {
    // 로그인 상태를 관리
    const [loggedIn, setLoggedIn] = useState(
        true  // 일단 true로 해둠, 로그인 구현하고 밑에 코드 쓰기
        //    localStorage.getItem('userId') ? true : false
    );

    useEffect(() => {
        const handleStorageChange = () => {
            setLoggedIn(localStorage.getItem('userId') ? true : false);
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    const [projects, setProjects] = useState([
        {
            id: 1, // id 추가
            name: '프로젝트 1',
            plAccount: 'PL1',
            testerAccount: 'Tester1',
            devAccount: 'Dev1',
            description: '프로젝트 1의 개요입니다.',
            createdAt: '2024-05-26',
            status: '진행중'
        },
        {
            id: 2, // id 추가
            name: '프로젝트 2',
            plAccount: 'PL2',
            testerAccount: 'Tester2',
            devAccount: 'Dev2',
            description: '프로젝트 2의 개요입니다.',
            createdAt: '2024-05-25',
            status: '완료'
        }
    ]);

    const [show, setShow] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);
    const [selectedIndex, setSelectedIndex] = useState(null);
    const [newProject, setNewProject] = useState({
        name: '',
        plAccount: '',
        testerAccount: '',
        devAccount: '',
        description: ''
    });

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
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
        const projectWithDate = {
            id: projects.length + 1, // 임의의 id 생성
            ...newProject,
            createdAt: new Date().toISOString().split('T')[0], // 현재 날짜를 설정
            status: '진행중'
        };
        setProjects([...projects, projectWithDate]);
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
    };

    return (
        <div className="main-container">
            <header className="header">
                <h1>프로젝트</h1>
                <div className="auth-buttons">
                    {loggedIn ? ( // 로그인 상태에 따라 다른 버튼 렌더링
                        <Button variant="primary" onClick={handleLogout}>로그아웃</Button>
                    ) : (
                        <>
                            <Link to="/login"><Button variant="primary">로그인</Button></Link>
                            <Link to="/signup"><Button variant="secondary">회원가입</Button></Link>
                        </>
                    )}
                </div>
            </header>
            {loggedIn && ( // 로그인 상태에 따라 테이블 렌더링 여부 결정
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>프로젝트 이름</th>
                            <th>개요</th>
                            <th>프로젝트 생성날짜</th>
                            <th>상태</th>
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
                                <td>
                                    <Button variant="danger" onClick={() => handleShowConfirm(index)}>...</Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
            {loggedIn && ( // 로그인 상태에 따라 생성 버튼 렌더링 여부 결정
                <div className="create-button">
                    <Button variant="success" onClick={handleShow}>프로젝트 생성</Button>
                </div>
            )}

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>새 프로젝트 추가</Modal.Title>
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
        </div>
    );
}

export default MainPage;