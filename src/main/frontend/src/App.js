import React from 'react';
import {Route, Routes} from 'react-router-dom';
import Joinform from './components/pages/Join/Joinform';
import SignIn from './components/pages/SignIn/SignIn';
import MainPage from "./components/pages/Mainpage/Mainpage";
import Test from './components/pages/util/Test';
import CustomNav from './components/pages/util/CustomNav';
import {RecoilRoot} from 'recoil';
import Mypage from './components/pages/Mypage/Mypage';
import PasswordValidation from './components/pages/Validation/PasswordValidation';
import UserModifyForm from './components/UserInfoModify/UserModifyForm';
import PasswordModiftForm from './components/UserInfoModify/PasswordModifyForm';


function App() {

    return (
        <>
            <RecoilRoot>
                <CustomNav/>
                <Routes>
                    <Route path='/' element={<MainPage/>}/>
                    <Route path='/join' element={<Joinform/>}/>
                    <Route path='/login/:accessToken?/:refreshToken?/:email?' element={<SignIn/>}/>
                    <Route path='/test' element={<Test/>}/>
                    <Route path='/mypage' element={<Mypage/>}/>
                    <Route path='/mypage/password/validation' element={<PasswordValidation/>}/>
                    <Route path='/mypage/info/modify' element={<UserModifyForm/>}/>
                    <Route path='/mypage/password/modify' element={<PasswordModiftForm/>} />
                </Routes>
            </RecoilRoot>
        </>
    );

}



export default App;
