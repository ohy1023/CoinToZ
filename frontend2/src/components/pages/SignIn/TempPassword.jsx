import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';
import { TextField } from '@mui/material';
import { useNavigate } from 'react-router-dom';


function TempPassword(props) {
  const navigate = useNavigate();
  const [code, setCode] = useState('');
  const email = props.email;


  function getCode(value) {
    setCode(value);
  }


  const Authenticate = async () => {
    await axios.get('/api/v1/users/password', {
      params:{
        code:code,
        email:email
      }
    }) 
      .then(function (response) {
        alert("임시비밀번호 : " + response.data.result);
        props.onHide();
        navigate('/login');
      }).catch(function (error) {
        console.log(error.response)
        alert("발급 실패했습니다.");
      })
  }

  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          이메일 인증
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          {email}로 인증번호를 보냈습니다.
        </p>
        <TextField
          required
          autoFocus
          fullWidth
          id="code"
          name="code"
          label="인증 번호"
          onChange={(e) => {
            getCode(e.target.value);
          }}
        />
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={() => {
          Authenticate()
        }}>임시 비밀번호 발급</Button>
        <Button onClick={props.onHide}>취소</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default TempPassword;