import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Auth.css';
import axios from 'axios';

const Register = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  const validateUsername = (username) => {
    return username.length >= 3 && username.length <= 20;
  };

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*\d)[A-Za-z\d]{8,30}$/;
    return passwordRegex.test(password);
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    let errorMessage = '';

    if (!validateUsername(username)) {
      errorMessage = 'Username must be between 3 and 20 characters.';
    } else if (!validatePassword(password)) {
      errorMessage = 'Password must be 8-30 characters long and include at least one number.';
    } else if (password !== confirmPassword) {
      errorMessage = 'Passwords do not match.';
    }

    if (errorMessage) {
      setError(errorMessage);
    } else {
      setError('');
      try {
        const response = await axios.post(`http://localhost:8080/api/${username}?password=${password}`, {
        });
        console.log(response.data);
        localStorage.setItem('username', response.data); 
        navigate('/');
      } catch (error) {
        console.error('Registration failed:', error);
      }
    }
  };

  return (
    <div className="auth-container">
      <img src="/logo.png" alt="Register" className="auth-image" />
      <div className="auth-card">
        {/* <h2>Register Page</h2> */}
        <form onSubmit={handleRegister}>
          <div className="auth-field">
            <label>Username:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="auth-field">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="auth-field">
            <label>Confirm Password:</label>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="auth-error">{error}</p>}
          <button type="submit" className="auth-button">Register</button>
        </form>
        <p className="auth-link">
          Have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;
