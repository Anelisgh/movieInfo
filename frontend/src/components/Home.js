import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { getWatchlists, getRecommendations } from '../services/watchlistApi';
import Navbar from './Navbar';
import '../styles/Home.css';
import '../styles/Watchlist.css';

const Home = () => {
  const [watchlists, setWatchlists] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const carouselRef = useRef(null);
  const watchlistsRef = useRef(null);

  const fetchData = async () => {
    try {
      // Încarcă recomandări indiferent de autentificare
      const recommendationsData = await getRecommendations();
      setRecommendations(recommendationsData);

      // Încarcă watchlists DOAR dacă există token
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const watchlistsData = await getWatchlists();
          setWatchlists(watchlistsData);
        } catch (error) {
          console.error("Error fetching watchlists:", error);
          // Șterge token-ul invalid și redirecționează
          if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
          }
        }
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  // Folosim aceeași funcție de scroll, dar adăugăm un parametr pentru a specifica containerul
  const scroll = (direction, ref) => {
    if (ref && ref.current) {
      const scrollAmount = 400;
      ref.current.scrollBy({
        left: direction === 'right' ? scrollAmount : -scrollAmount,
        behavior: 'smooth'
      });
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className='main-content home-page'>
      <Navbar />
      <div className='content'>
        {watchlists.length > 0 && (
          <>
            <h2>Watchlists</h2>
          <div className="watchlists-scroll-container">
            <button
              className="scroll-arrow left"
              onClick={() => scroll("left", watchlistsRef)}
            >
              <img src="/icons/arrow_scroll_l.png" alt="Scroll left" />
            </button>
            <div className="watchlists-container" ref={watchlistsRef}>
              {watchlists.map((watchlist) => (
                <Link
                  to={`/watchlist/${watchlist.id}`}
                  key={watchlist.id}
                  className="watchlist-link"
                >
                  <div className="watchlist-card">
                    <div className="movies-grid">
                      {watchlist.movies.slice(0, 4).map((movie, index) => (
                        <div
                          key={`${watchlist.id}-${index}`}
                          className="movie-thumbnail"
                        >
                          {movie.photoUrl ? (
                            <img
                              src={movie.photoUrl}
                              alt={movie.title}
                              onError={(e) => {
                                e.target.onerror = null;
                                e.target.src = "/placeholder.jpg";
                              }}
                            />
                          ) : (
                            <div className="placeholder">{movie.title}</div>
                          )}
                        </div>
                      ))}

                      {watchlist.movies.length < 4 &&
                        Array(4 - watchlist.movies.length)
                          .fill()
                          .map((_, index) => (
                            <div
                              key={`placeholder-${watchlist.id}-${index}`}
                              className="movie-thumbnail invisible"
                            ></div>
                          ))}
                    </div>
                    <div className="watchlist-title">
                      <h3>{watchlist.name}</h3>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
            <button
              className="scroll-arrow right"
              onClick={() => scroll("right", watchlistsRef)}
            >
              <img src="/icons/arrow_scroll_r.png" alt="Scroll right" />
            </button>
          </div>

          </>
        )}

        <h2>Your Next Watch</h2>
        <div className="recommendations-container">
          <button className="scroll-arrow left" onClick={() => scroll('left', carouselRef)}>
            <img src="/icons/arrow_scroll_l.png" alt="Scroll left" />
          </button>
          <div className="movies-carousel" ref={carouselRef}>
            {recommendations.map((movie) => (
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
                  <p className="genre">{movie.genre.toLowerCase()}</p>
                </div>
              </Link>
            ))}
          </div>
          <button className="scroll-arrow right" onClick={() => scroll('right', carouselRef)}>
            <img src="/icons/arrow_scroll_r.png" alt="Scroll right" />
          </button>
        </div>

      </div>
    </div>
  );
};

export default Home;