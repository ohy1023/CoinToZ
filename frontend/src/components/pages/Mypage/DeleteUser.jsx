import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal, Button } from 'react-bootstrap';
import { privateApi } from '../../../utils/http-common';
import { useNavigate } from 'react-router-dom';

function DeleteUser(props) {
  const navigate = useNavigate();

  const deleteUser = async () => {
    privateApi
      .delete('/api/v1/users')
      .then(function (response) {
        localStorage.removeItem('email');
        localStorage.removeItem('userName');
        localStorage.removeItem('imageUrl');
        localStorage.removeItem('createAt');
        alert(response.data.result);
        navigate('/');
      })
      .catch(function (error) {
        alert('탈퇴 실패');
      });
  };

  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">회원 탈퇴</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>정말 탈퇴 하시겠습니까?</p>
      </Modal.Body>
      <Modal.Footer>
        <Button
          onClick={() => {
            deleteUser();
            props.onHide();
          }}
        >
          탈퇴
        </Button>
        <Button onClick={props.onHide}>취소</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default DeleteUser;
