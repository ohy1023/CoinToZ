import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Joinform from './components/pages/Join/Joinform';
import SignIn from './components/pages/SignIn/SignIn';
import Main from "./components/pages/Mainpage/Mainpage";
import Test from './components/pages/util/Test';
import { RecoilRoot } from 'recoil';
import Mypage from './components/pages/Mypage/Mypage';
import PasswordValidation from './components/pages/Validation/PasswordValidation';
import UserModifyForm from './components/pages/UserInfoModify/UserModifyForm';
import PasswordModiftForm from './components/pages/UserInfoModify/PasswordModifyForm';
import UpbitMainPage from './components/pages/UpbitMainPage/UpbitMainPage';
import Dashboard from './components/pages/Diary/Dashboard';
import NavRestApi from './components/common/ApiNav/NavRestApi';
import MarketCode from './QuotationAPI/RESTAPI-example/MarketCode';
import DayCandleData from './QuotationAPI/RESTAPI-example/DayCandleData';
import MinuteCandleData from './QuotationAPI/RESTAPI-example/MinuteCandleData';
import WeekMonthCandleData from './QuotationAPI/RESTAPI-example/WeekMonthCandleData';
import TradeHistoryData from './QuotationAPI/RESTAPI-example/TradeHistoryData';
import NavWebsocketApi from './components/common/ApiNav/NavWebsocketApi';
import OrderBook from 'upbit-api/container/OrderBook';
import RealTimePrice from './QuotationAPI/WEBSOCKET-example/RealTimePrice';
import TradeHistory from './QuotationAPI/WEBSOCKET-example/TradeHistory';
import TotalExample from './QuotationAPI/TOTAL-example/TotalExample';
import Payment from './components/pages/PaymentPage/Payment';
import NewPassword from './components/pages/SignIn/NewPassword';
import Board from './components/pages/Community/Board';
import MyBoard from './components/pages/Mypage/MyBoard';
import PostDetail from './components/pages/Community/PostDetail';
import PostUpdate from './components/pages/Community/PostUpdate';
import PostWrite from './components/pages/Community/PostWrite';
import UpbitKey from './components/pages/Mypage/UpbitKeyForm';
import Wage from './components/pages/Mypage/Wage';
import Withdraw from './components/pages/Mypage/Withdraw';
import Information from './components/pages/UpbitKeyInformation/Information';
import AppBar from './components/common/TitleMenu/AppBar';
import Footer from './components/common/Footer/Footer';


function App() {

    return (
        <>
            <RecoilRoot>
                <AppBar/>
                <Routes>
                    <Route path='/' element={<Main />} />
                    <Route path='/join' element={<Joinform />} />
                    <Route path='/login/:accessToken?/:refreshToken?/:email?' element={<SignIn />} />
                    <Route path='/test' element={<Test />} />
                    <Route path='/mypage' element={<Mypage />} />
                    <Route path='/mypage/password/validation' element={<PasswordValidation />} />
                    <Route path='/mypage/info/modify' element={<UserModifyForm />} />
                    <Route path='/mypage/password/modify' element={<PasswordModiftForm />} />
                    <Route path='/mypage/myboard' element={<MyBoard />} />
                    <Route path='/mypage/upbitkey' element={<UpbitKey/>} />
                    <Route path='/mypage/wage' element={<Wage/>} />
                    <Route path='/mypage/withdraw' element={<Withdraw/>} />
                    
                    <Route path='/upbitMainPage' element={<UpbitMainPage/>}/>
                    <Route path='/upbit/infomation' element={<Information/>}/>
                    <Route path='/payment' element={<Payment />} />
                    <Route path='/diary' element={<Dashboard />} />
                    <Route path='/temp' element={<NewPassword />} />

                    <Route path='/community' element={<Board />} />
                    <Route path="/postDetail/:postId" element={<PostDetail />} />
                    <Route path="/postUpdate" element={<PostUpdate />}/>
                    <Route path="/post" element={<PostWrite />} />

                    <Route path="rest-api" element={<NavRestApi />}>
                        <Route path="marketcode" element={<MarketCode />} />
                        <Route path="day-candle-data" element={<DayCandleData />} />
                        <Route path="minute-candle-data" element={<MinuteCandleData />} />
                        <Route path="week-month-candle-data" element={<WeekMonthCandleData />} />
                        <Route path="trade-history-data" element={<TradeHistoryData />} />
                    </Route>
                    <Route path="websocket-api" element={<NavWebsocketApi />}>
                        <Route path="order-book" element={<OrderBook />} />
                        <Route path="real-time-price" element={<RealTimePrice />} />
                        <Route path="trade-history" element={<TradeHistory />} />
                    </Route>
                    <Route
                        path="total-example"
                        element={
                            <RecoilRoot>
                                <TotalExample />
                            </RecoilRoot>} />
                    </Routes>
                <Footer/>
            </RecoilRoot>
        </>
    );

}



export default App;
