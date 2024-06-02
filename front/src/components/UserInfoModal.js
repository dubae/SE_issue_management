import React from 'react';
import { Modal, Button } from 'react-bootstrap';

function UserInfoModal({ show, handleClose, userInfo }) {
    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>내 정보</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p><strong>아이디:</strong> {userInfo.userid}</p>
                <p><strong>이메일:</strong> {userInfo.email}</p>
                <p><strong>이름:</strong> {userInfo.username}</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    닫기
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default UserInfoModal;
