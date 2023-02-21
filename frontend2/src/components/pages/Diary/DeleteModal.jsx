import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Modal,Button} from 'react-bootstrap';
import Api from '../../../functions/customApi';

function DeleteModal(props) {

  const deleteMemo = () => {
    Api.delete(`api/v1/diary/delete/${props.diaryid}`)
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
          메모 삭제 
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
           정말 삭제 하시겠습니까?
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={()=>{
          deleteMemo()
          props.onHide()
          props.onExit()
          }}>삭제</Button>
        <Button onClick={props.onHide}>취소</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default DeleteModal;