import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/api';
import { updateProfile, updatePassword, deleteAccount } from '../services/userApi';
import Navbar from './Navbar';
import '../styles/ProfileSettings.css';

const ProfileSettings = () => {
    const [profileData, setProfileData] = useState({ name: '', email: '' });
    const [passwordData, setPasswordData] = useState({ oldPassword: '', newPassword: '' });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem('token');
                console.log('Fetching user data with token:', token);
                
                if (!token) {
                    console.log('No token found, redirecting...');
                    navigate('/login');
                    return;
                }
    
                const response = await API.get('/auth/me');
                console.log('User data response:', response.data);
                
                setProfileData({
                    name: response.data.data.name,
                    email: response.data.data.email
                });
            } catch (error) {
                console.error('Profile fetch error:', error);
                if (error.response?.status === 401 || error.response?.status === 403) {
                    localStorage.removeItem('token');
                    navigate('/login');
                } else {
                    setError('Unable to load profile data');
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

    const handleDeleteAccount = async () => {
        try {
            await deleteAccount();
            localStorage.removeItem('token');
            navigate('/login');
        } catch (error) {
            setError('Failed to delete account');
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
            
            <div className="danger-zone">
                    <button 
                        type="button" 
                        className="delete-btn"
                        onClick={() => setShowDeleteModal(true)}
                    >
                        Delete Account
                    </button>
                    </div>
                    {showDeleteModal && (
                    <div className="delete-modal-overlay">
                        <div className="delete-modal-content">
                            <h3>Confirm Account Deletion</h3>
                            <p>Are you sure you want to delete your account? This action cannot be undone.</p>
                            <div className="modal-buttons">
                                <button 
                                    className="modal-btn confirm-btn"
                                    onClick={handleDeleteAccount}
                                >
                                    Delete Permanently
                                </button>
                                <button
                                    className="modal-btn cancel-btn"
                                    onClick={() => setShowDeleteModal(false)}
                                >
                                    Cancel
                                </button>
                            </div>
                        </div>
                    </div>
                    )}
            </div>
        </div>
    );
};

export default ProfileSettings;
