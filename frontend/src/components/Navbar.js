import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

const Navbar = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem('token');
      setIsLoggedIn(!!token);
    };
    
    checkAuth();
    window.addEventListener('storage', checkAuth);
    
    return () => window.removeEventListener('storage', checkAuth);
  }, []);

  
const handleSearch = (e) => {
  e.preventDefault();
  navigate(`/search?title=${searchQuery}`);
};

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    window.location.href = '/login';
  };

  return (
    <header className="main-header">
      <div className="header-content">
        <Link to="/">
          <div className="logo-container">
            <img src="/icons/icons8-movie-48.png" alt="Pre MovieInfo Logo" className="pre-logo-image" />
            <h1 className="logo">
              <img src="/icons/MovieInfo.svg" alt="MovieInfo Logo" className="logo-image" />
            </h1>
          </div>
        </Link>

        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            className="search-input"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleSearch(e);
              }
            }}
            placeholder="Search..."
          />
        </form>

        <nav className="main-nav">
          <Link to="#" onClick={handleSearch} className="nav-link">
            <img src="/icons/icons8-search.svg" alt="Search" className="nav-icon" /> Search
          </Link>
          <Link to="/watchlist" className="nav-link">
            <img src="/icons/watchlist.png" alt="WatchList" className="nav-icon" /> WatchList
          </Link>
          <Link to="/add-movie" className="nav-link">
            <img src="/icons/add.png" alt="Add Movies" className="nav-icon" /> Add Movies
          </Link>
          <Link to="/profile" className="nav-link">
            <img src="\icons\account.png" alt="Account" className='nav-icon' /> Account
          </Link>

          {isLoggedIn ? (
          <button 
            onClick={handleLogout} 
            className="nav-link" 
            style={{ background: 'none', border: 'none', cursor: 'pointer', font: 'inherit', padding: 0 }}
          >
            <img src="/icons/login.png" alt="Logout Icon" className="nav-icon" /> Logout
          </button>
        ) : (
          <Link to="/login" className="nav-link">
            <img src="/icons/login.png" alt="Log in Icon" className="nav-icon" /> Log in
          </Link>
        )}

        </nav>
      </div>
    </header>
  );
};

export default Navbar;