import React from 'react';
import {Route, Routes} from 'react-router-dom';
import Joinform from './components/pages/Join/Joinform';
import SignIn from './components/pages/SignIn/SignIn';
import MainPage from "./components/pages/Mainpage/Mainpage";
import Test from './components/pages/util/Test';
import CustomNav from './components/pages/util/CustomNav';
import {RecoilRoot} from 'recoil';


function App() {

    return (
        <>
            <RecoilRoot>
                <CustomNav/>
                <Routes>
                    <Route path='/' element={<MainPage/>}/>
                    <Route path='/join' element={<Joinform/>}/>
                    <Route path='/login' element={<SignIn/>}/>
                    <Route path='/test' element={<Test/>}/>
                </Routes>
            </RecoilRoot>
        </>
    );

}



export default App;
