import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal, Button, Form } from 'react-bootstrap';
import Api from '../../../functions/customApi';

function UpdateModal(props) {

  const [comment, setComment] = useState("")

  const handleComment = (e) => {
    setComment(e.target.value);
  };

  const update = () => {
    Api.put(`api/v1/diary/edit/${props.diaryid}`, {
      comment: comment,
    })
      .then(function(response){
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
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
          메모 작성
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h4>메모 작성</h4>
        <p>
          <Form.Control
            onChange={handleComment}
          />
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={() => {
          update()
          props.onHide()
          props.onExit()
        }}>수정</Button>
        <Button onClick={props.onHide}>취소</Button>
      </Modal.Footer>
    </Modal>

  );
}

export default UpdateModal;