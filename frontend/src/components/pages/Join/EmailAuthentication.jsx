import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';
import { TextField } from '@mui/material';


function EmailAuthentication(props) {

  const [code,setCode] = useState('');

  // 입력한 인증 코드 설정 함수
  function getCode(value) {
    setCode(value);
  }

  // 인증번호 일치 한지 확인 함수
  const Authenticate = async () => {
    console.log(code); // 코드 콘솔에 나오게
    axios.get('/api/v1/emails/auth', {
      params: {code}
    }) // 수정해야 할 부분
      .then(function (response) {
        props.set(); // 이메일 체크 true로 바꾸는 함수
        alert("인증되었습니다.");
        props.onHide();
      }).catch(function (error) {
        alert("인증 실패");
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
          {props.email}로 인증번호를 보냈습니다.
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
        }}>인증</Button>
        <Button onClick={() => {
          props.onHide()
        }}>취소</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default EmailAuthentication;