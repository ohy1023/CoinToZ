import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Nav } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { userState } from './GlobalState';
import AccountPopover from './profileSelect';


const CustomNav = () => {
  const navigate = useNavigate();

  const user = useRecoilValue(userState);

  return (
    <Navbar bg="dark" variant="dark">>
      <Navbar.Brand><Nav.Link onClick={() => { navigate('/') }} >Justock</Nav.Link></Navbar.Brand>
      <Nav className="me-auto">
        <Nav.Link onClick={() => { navigate('/test') }}>토큰 테스트</Nav.Link>
      </Nav>
      <Nav style={{ margin: '10px' }}>
        {user ? (
          <>
            <AccountPopover />
          </>
        ) : (
          <>
            <Nav.Link onClick={() => { navigate('/join') }}>회원가입</Nav.Link>
            <Nav.Link onClick={() => { navigate('/login') }}>로그인</Nav.Link>
          </>)}
      </Nav>
    </Navbar>
  );
};

export default CustomNav;