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
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add('auth-page');
    return () => {
      document.body.classList.remove('auth-page');
    };
  }, []);  

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    if (userData.password !== userData.confirmPassword) {
      setErrors({ confirmPassword: "Passwords do not match!" });
      return;
    }
    try {
      await registerUser(userData);
      navigate('/login');
    } catch (error) {
      const backendError = error.response?.data?.error;
      if (backendError?.includes("Validation failed")) {
        // Parsează erorile de validare din backend
        const parsedErrors = parseValidationErrors(backendError);
        setErrors(parsedErrors);
      } else {
        setErrors({ general: backendError || 'Registration failed.' });
      }
    }
  };

  const parseValidationErrors = (errorMessage) => {
    const errors = {};
    const validationParts = errorMessage.replace("Validation failed: ", "").split(/,\s*/);
    
    validationParts.forEach(part => {
      const match = part.match(/(.+?):\s*(.+)/);
      if (match && match.length === 3) {
        const field = match[1].trim();
        const message = match[2].trim();
        errors[field] = message;
      }
    });
    return errors;
  };

  return (
    <div className="login-container">
      <Navbar />
      <div className="auth-wrapper">
        <div className="auth-card glass-effect">
          <h2 className="auth-title">Sign up</h2>
          {errors.general && <div className="error-message general-error">{errors.general}</div>}
          <form onSubmit={handleSubmit} className="auth-form">
            {/* Câmpul pentru Name */}
            <div className="form-group">
            <label htmlFor="name" className="form-label">Username</label>
              <input
                id="name"
                type="text"
                className={`form-input ${errors.name ? 'input-error' : ''}`}
                onChange={(e) => setUserData({ ...userData, name: e.target.value })}
              />
              {errors.name && <div className="error-message">{errors.name}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="email" className="form-label">Email</label>
              <input
                id="email"
                type="email"
                className={`form-input ${errors.email ? 'input-error' : ''}`}
                onChange={(e) => setUserData({ ...userData, email: e.target.value })}
              />
              {errors.email && <div className="error-message">{errors.email}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="password" className="form-label">Password</label>
              <input
                id="password"
                type="password"
                className={`form-input ${errors.password ? 'input-error' : ''}`}
                onChange={(e) => setUserData({ ...userData, password: e.target.value })}
              />
              {errors.password && <div className="error-message">{errors.password}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword" className="form-label">Confirm Password</label>
              <input
                id="confirmPassword"
                type="password"
                className={`form-input ${errors.confirmPassword ? 'input-error' : ''}`}
                onChange={(e) => setUserData({ ...userData, confirmPassword: e.target.value })}
              />
              {errors.confirmPassword && <div className="error-message">{errors.confirmPassword}</div>}
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