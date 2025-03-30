import React, {useEffect } from 'react';
import API from './services/api';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import Home from './components/Home';
import PrivateRoute from './components/PrivateRoute';
import AddMovie from './components/AddMovie';
import MovieDetails from './components/MovieDetails';
import SearchResults from './components/SearchResults';
import Watchlist from './components/Watchlist';
import WatchlistDetails from './components/WatchlistDetails';
import ProfileSettings from './components/ProfileSettings';

function App() {
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      API.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      console.log('Token set on app startup');
    }
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/add-movie" element={<PrivateRoute><AddMovie /></PrivateRoute>} />
        <Route path="/movies/:movieId" element={<MovieDetails />} /> 
        <Route path="/search" element={<SearchResults />} />
        <Route path="/watchlist" element={<PrivateRoute><Watchlist /></PrivateRoute>} />
        <Route path="/watchlist/:id" element={<PrivateRoute><WatchlistDetails /></PrivateRoute>} />
        <Route path="/profile" element={<PrivateRoute><ProfileSettings /></PrivateRoute>} />
      </Routes>
    </Router>
  );
}

export default App;
