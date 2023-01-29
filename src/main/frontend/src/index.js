import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <RecoilRoot>
        <Routes>
          <Route path="/*" element={ <App /> }></Route>
        </Routes>
      </RecoilRoot>
    </BrowserRouter>
  </React.StrictMode>
);
