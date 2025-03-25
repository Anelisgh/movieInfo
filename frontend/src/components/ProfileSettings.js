import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/api';
import { updateProfile, updatePassword } from '../services/userApi';
import Navbar from './Navbar';
import '../styles/ProfileSettings.css';

const ProfileSettings = () => {
    const [profileData, setProfileData] = useState({ name: '', email: '' });
    const [passwordData, setPasswordData] = useState({ oldPassword: '', newPassword: '' });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    navigate('/login');
                    return;
                }

                const response = await API.get('/auth/me', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                const userData = response.data.data;
                setProfileData({
                    name: userData.name,
                    email: userData.email,
                });
            } catch (error) {
                console.error('Error fetching user data:', error);
                setError('Unable to load profile data');
                if (error.response && error.response.status === 401) {
                    navigate('/login');
                }
            }
        };

        fetchUserData();
    }, [navigate]);

    const handleProfileSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await updateProfile(profileData);
            console.log('Response from backend:', response.data); // Loghează răspunsul
            
            // Afișează mesajul de succes
            setSuccess('Profile updated successfully! Please log in again.');
            setError('');
            
            // Deloghează utilizatorul imediat
            console.log('Logging out and redirecting to login...'); // Loghează delogarea
            localStorage.removeItem('token'); // Șterge token-ul
            navigate('/login'); // Redirecționează către login
        } catch (error) {
            setError('Error updating profile');
            setSuccess('');
        }
    };

    const handlePasswordSubmit = async (e) => {
        e.preventDefault();
        try {
            await updatePassword(passwordData);
            setSuccess('Password updated successfully!');
            setError('');
            setPasswordData({ oldPassword: '', newPassword: '' });
        } catch (error) {
            setError('The old password is incorrect');
            setSuccess('');
        }
    };

    return (
        <div className="profile-settings-page">
            <Navbar />
            <div className="content">
                <h1>Account Settings</h1>

                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}
                <form onSubmit={handleProfileSubmit} className="profile-form">
                    <h2>Update Profile</h2>
                    <div className="form-group">
                    <div className="form-icon-label">
                        <img src="\icons\username.png" alt="Username" className='form-icons'/>
                        <label>Username</label>
                    </div>
                    <input
                        type="text"
                        value={profileData.name}
                        onChange={(e) => setProfileData({ ...profileData, name: e.target.value })}
                    />
                </div>
                    <div className="form-group">
                    <div className="form-icon-label">
                        <img src="\icons\email.png" alt="Email" className='form-icons'/>
                        <label>Email</label>
                        </div>
                        <input
                            type="email"
                            value={profileData.email}
                            onChange={(e) => setProfileData({ ...profileData, email: e.target.value })}
                        />
                    </div>
                    <button type="submit" className="submit-btn">Save</button>
                </form>

                <form onSubmit={handlePasswordSubmit} className="password-form">
                    <h2>Change Password</h2>
                    <div className="form-group">
                    <div className="form-icon-label">
                        <img src="\icons\password.png" alt="password" className='form-icons'/>
                        <label>Old Password</label>
                    </div>
                        <input
                            type="password"
                            value={passwordData.oldPassword}
                            onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
                        />
                    </div>
                    <div className="form-group">
                    <div className="form-icon-label">
                    <img src="\icons\password.png" alt="password" className='form-icons'/>
                        <label>New Password</label>
                    </div>
                        <input
                            type="password"
                            value={passwordData.newPassword}
                            onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                        />
                    </div>
                    <button type="submit" className="submit-btn">Confirm</button>
                </form>
            </div>
        </div>
    );
};

export default ProfileSettings;
