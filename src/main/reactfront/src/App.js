import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './Login';
import Signup from './Signup';
import MainPage from './MainPage';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const login = () => {
        setIsAuthenticated(true);
    };

    const register = () => {
        // 회원가입 성공 시 로그인 상태로 설정
        setIsAuthenticated(true);
    };

    return (
        <Router>
            <Routes>
                <Route 
                    path="/" 
                    element={isAuthenticated ? <Navigate to="/main" /> : <Login login={login} />} 
                />
                <Route 
                    path="/signup" 
                    element={isAuthenticated ? <Navigate to="/main" /> : <Signup register={register} />} 
                />
                <Route 
                    path="/main" 
                    element={isAuthenticated ? <MainPage /> : <Navigate to="/" />} 
                />
            </Routes>
        </Router>
    );
}

export default App;
