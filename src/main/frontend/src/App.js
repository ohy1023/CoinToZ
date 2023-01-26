import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Navbar,Nav} from 'react-bootstrap';
import {Route, Routes, useNavigate} from 'react-router-dom';
import Joinform from './components/pages/Join/Joinform';
import SignIn from './components/pages/SignIn/SignIn';
import MainPage from "./components/pages/Mainpage/Mainpage";


function App() {

    const navigate = useNavigate();

    return (
        <>
            <Navbar bg="dark" variant="dark">
                <Navbar.Brand style={{margin:'10px'}}><Nav.Link onClick={()=>{navigate('/')}} >Justock</Nav.Link></Navbar.Brand>
                <Nav className="me-auto">
                    <Nav.Link onClick={()=>{navigate('/join')}}>회원가입</Nav.Link>
                    <Nav.Link onClick={()=>{navigate('/login')}}>로그인</Nav.Link>
                </Nav>
            </Navbar>
            <Routes>
                <Route path='/' element={<MainPage/>}/>
                <Route path='/join' element={<Joinform/>}/>
                <Route path='/login' element={<SignIn/>}/>
            </Routes>
        </>
    );

}



export default App;
