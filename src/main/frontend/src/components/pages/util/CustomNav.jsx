import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Nav } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { RecoilRoot, useRecoilState } from 'recoil';
import { userState } from './GlobalState';
import { removeCookie } from './cookie';
import Api from './customApi';


const CustomNav = () => {
  const navigate = useNavigate();
  
  const [user, setUser] = useRecoilState(userState);

  const logoutUser = async () => {
    await Api.get("/api/v1/users/logout")
    .then(function (response) {
      removeCookie('access');
      removeCookie('refresh');
      localStorage.removeItem('email');
      setUser('');
      alert("로그아웃이 완료되었습니다.");
      navigate('/');
    })
    .catch(function (err) {
      console.log(err);
      alert("로그아웃 실패!");
    });
    
  }

  return (
    <RecoilRoot>
      <Navbar bg="dark" variant="dark">>
        <Navbar.Brand style={{margin:'10px'}}><Nav.Link onClick={()=>{navigate('/')}} >Justock</Nav.Link></Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link onClick={()=>{navigate('/test')}}>토큰 테스트</Nav.Link>
          </Nav>
          <Nav>
          {user ? (<Nav.Link onClick={logoutUser}>로그아웃</Nav.Link>) : (
            <>
              <Nav.Link onClick={()=>{navigate('/join')}}>회원가입</Nav.Link>
              <Nav.Link onClick={()=>{navigate('/login')}}>로그인</Nav.Link>
            </>)}
          </Nav>
      </Navbar>
    </RecoilRoot>
  );
};

export default CustomNav;