import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import Navbar from './Navbar';
import { searchMovies } from '../services/api';
import '../styles/SearchResults.css';

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const searchQuery = searchParams.get('title'); // Schimbăm din 'query' în 'title'

  // În SearchResults.js, adaugă loguri de debugging:
  useEffect(() => {
    const fetchResults = async () => {
      try {
        setLoading(true);
        console.log('Searching for:', searchQuery);
        const data = await searchMovies(searchQuery);
        console.log('Received data:', data);
        setMovies(data);
      } catch (error) {
        console.error('Search error:', error.response?.data || error.message);
        setMovies([]);
      } finally {
        setLoading(false); // Adăugăm asta
      }
    };
    fetchResults();
  }, [searchQuery]);

  return (
    <div className="search-results-page">
      <Navbar />
      
      <main className="results-container">
        <h2 className="results-title">Here's what we found</h2>
        
        {loading ? (
          <div className="loading">Loading...</div>
        ) : (
          <>
            <div className="movies-grid">
              {movies.map((movie) => (
                <Link 
                  to={`/movies/${movie.id}`} 
                  key={movie.id}  
                  className="movie-card"
                >
                  <div className="movie-poster">
                    {movie.photoUrl ? (
                      <img 
                        src={movie.photoUrl} 
                        alt={movie.title} 
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = '/placeholder.jpg';
                        }} 
                      />
                    ) : (
                      <div className="placeholder-poster">{movie.title}</div>
                    )}
                  </div>
                  <div className="movie-info">
                    <h4>{movie.title}</h4>
                  </div>
                </Link>
              ))}
            </div>

            <div className="missing-movie">
              <p>The movie you're looking for is missing?</p>
              Add it from <Link to="/add-movie" className="add-movie-link">this page</Link>
            </div>
          </>
        )}
      </main>
    </div>
  );
};

export default SearchResults;