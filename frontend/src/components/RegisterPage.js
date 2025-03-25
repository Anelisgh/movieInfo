import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../services/api';
import Navbar from './Navbar';
import '../styles/Auth.css';

const RegisterPage = () => {
  const [userData, setUserData] = useState({
    name: '',       // Schimbat din username în name
    email: '',      // Adăugat câmpul email
    password: '',
    confirmPassword: ''
  });
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add('auth-page');
    return () => {
      document.body.classList.remove('auth-page');
    };
  }, []);  

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (userData.password !== userData.confirmPassword) {
      alert("Passwords don't match!");
      return;
    }
    try {
      await registerUser(userData);
      alert('Registration successful!');
      navigate('/login');
    } catch (error) {
      const errorMessage = error.response?.data?.message || 'Registration failed';
      alert(errorMessage);
    }
  };

  return (
    <div className="login-container">
      <Navbar />
      <div className="auth-wrapper">
        <div className="auth-card glass-effect">
          <h2 className="auth-title">Sign up</h2>
          <form onSubmit={handleSubmit} className="auth-form">
            {/* Câmpul pentru Name */}
            <div className="form-group">
              <label htmlFor="username" className="form-label">Username</label>
              <input
                id="username"
                type="text"
                className="form-input"
                onChange={(e) => setUserData({ ...userData, name: e.target.value })}
                required
              />
            </div>

            {/* Câmpul pentru Email */}
            <div className="form-group">
              <label htmlFor="email" className="form-label">Email</label>
              <input
                id="email"
                type="email"
                className="form-input"
                onChange={(e) => setUserData({ ...userData, email: e.target.value })}
                required
              />
            </div>

            {/* Câmpurile pentru Parolă */}
            <div className="form-group">
              <label htmlFor="password" className="form-label">Password</label>
              <input
                id="password"
                type="password"
                className="form-input"
                onChange={(e) => setUserData({ ...userData, password: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
              <input
                id="confirmPassword"
                type="password"
                className="form-input"
                onChange={(e) => setUserData({ ...userData, confirmPassword: e.target.value })}
                required
              />
            </div>

            <button type="submit" className="submit-btn">
              Register
              <img src="/icons/arrow.png" alt="arrow" className="btn-arrow" />
            </button>

            <p>Already have an account? <a href="/login">Login here</a></p>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;