import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../services/api';
import Navbar from './Navbar'; // Importă Navbar
import '../styles/Auth.css'; // Fișier CSS separat pentru stilizare

const LoginPage = () => {
  const [credentials, setCredentials] = useState({ 
    username: '', 
    password: '', 
    rememberMe: false 
  });
  
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.getItem('token')) {
      navigate('/');
    }
  }, [navigate]);

  useEffect(() => {
    document.body.classList.add('auth-page');
    return () => {
      document.body.classList.remove('auth-page');
    };
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const responseData = await loginUser(credentials);
      if (responseData.token) { 
        localStorage.setItem('token', responseData.token);
        navigate('/'); 
      }
    } catch (error) {
      console.error('Login error:', error.response?.data || error.message);
    }
  };

  return (
    <div className="login-container">
      <Navbar /> {/* Folosește componenta Navbar */}
      <div className="auth-wrapper">
        <div className="auth-card glass-effect">
          <h2 className="auth-title">Login</h2>
          <form onSubmit={handleSubmit} className="auth-form">
            <div className="form-group">
              <label htmlFor="username" className="form-label">Username</label>
              <input
                id="username"
                type="text"
                className="form-input"
                onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
              />
            </div>

            <div className="form-group">
              <label htmlFor="password" className="form-label">Password</label>
              <input
                id="password"
                type="password"
                className="form-input"
                onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
              />
            </div>

            <div className="remember-group">
              <input
                type="checkbox"
                id="rememberMe"
                className="checkbox"
                checked={credentials.rememberMe}
                onChange={(e) => setCredentials({ ...credentials, rememberMe: e.target.checked })}
              />
              <label htmlFor="rememberMe">Remember me</label>
            </div>

            <button type="submit" className="submit-btn">
              Sign In
              <img src="/icons/arrow.png" alt="arrow" className="btn-arrow" />
            </button>

            <p className="register-link">
              Don't have an account yet? <a href="/register">Register here</a>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
