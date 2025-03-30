import React, { useState, useEffect, useRef } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getWatchlistById, getWatchlists, moveMoviesToWatchlist, removeMoviesFromWatchlist, updateWatchlist } from '../services/watchlistApi';
import Navbar from './Navbar';
import '../styles/WatchlistDetails.css';

const WatchlistDetails = () => {
  const { id } = useParams();
  const [watchlist, setWatchlist] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isEditMode, setIsEditMode] = useState(false);
  const [selectedMovies, setSelectedMovies] = useState([]);
  const [showMoveDropdown, setShowMoveDropdown] = useState(false);
  const [allWatchlists, setAllWatchlists] = useState([]);
  const [newName, setNewName] = useState('');
  const timeoutRef = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [currentWatchlist, allWatchlistsData] = await Promise.all([
          getWatchlistById(id),
          getWatchlists()
        ]);
        
        setWatchlist(currentWatchlist);
        setAllWatchlists(allWatchlistsData.filter(w => w.id !== Number(id)));
        setNewName(currentWatchlist.name);
        setError('');
      } catch (err) {
        setError('Failed to load data');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleInput = (e) => {
    const selection = window.getSelection();
    const range = selection.getRangeAt(0);
    const cursorPosition = range.startOffset;
    
    setNewName(e.target.textContent);
    
    // După re-render, restaurează poziția cursorului
    setTimeout(() => {
      const newRange = document.createRange();
      const textNode = e.target.firstChild;
      newRange.setStart(textNode, Math.min(cursorPosition, textNode.length));
      newRange.collapse(true);
      selection.removeAllRanges();
      selection.addRange(newRange);
    }, 0);
  };

  const handleEditToggle = () => {
    if (isEditMode) {
      // Save watchlist name if changed
      if (newName !== watchlist.name) {
        updateWatchlist(id, { name: newName, movieIds: watchlist.movies.map(m => m.id) })
          .then(() => setWatchlist(prev => ({ ...prev, name: newName })))
          .catch(error => {
            console.error('Failed to update watchlist name:', error);
            alert('Failed to update watchlist name. Please try again.');
          });
      }
    }
    setIsEditMode(!isEditMode);
    setSelectedMovies([]);
    setShowMoveDropdown(false);
  };

  const handleMovieSelect = (movieId) => {
    setSelectedMovies(prev => 
      prev.includes(movieId) 
        ? prev.filter(id => id !== movieId) 
        : [...prev, movieId]
    );
  };

  const handleMoveMovies = (targetWatchlistId) => {
    if (selectedMovies.length === 0) {
      alert('Please select at least one movie to move.');
      return;
    }
  
    moveMoviesToWatchlist(id, targetWatchlistId, selectedMovies)
      .then(() => {
        setWatchlist(prev => ({
          ...prev,
          movies: prev.movies.filter(m => !selectedMovies.includes(m.id))
        }));
        setSelectedMovies([]);
        setShowMoveDropdown(false);
      })
      .catch(error => {
        console.error('Failed to move movies:', error);
        alert('Failed to move movies. Please try again.');
      });
  };

  const handleRemoveMovies = () => {
    removeMoviesFromWatchlist(id, selectedMovies)
      .then(() => {
        setWatchlist(prev => ({
          ...prev,
          movies: prev.movies.filter(m => !selectedMovies.includes(m.id))
        }));
        setSelectedMovies([]);
      });
  };

  const handleMouseEnter = () => {
    if (timeoutRef.current) clearTimeout(timeoutRef.current); // Anulează timer-ul dacă există
    setShowMoveDropdown(true);
  };

  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => setShowMoveDropdown(false), 300); // Delay de 300ms
  };


  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!watchlist) return <div className="error">Watchlist not found</div>;

  return (
    <div className="watchlist-details-page">
      <Navbar />
      <div className="content">
        <div className="header-watchlist">
          {isEditMode ? (
            <div className="editable-watchlist-name">
              <div
                contentEditable
                suppressContentEditableWarning
                onInput={handleInput}
                className="edit-name-display"
              >
                {newName}
              </div>
              <img
                src="/icons/edit_name_watchlist.svg"
                alt="Edit name"
                className="edit-name-icon"
              />
            </div>
          ) : (
            <h1 className="watchlist-title">{watchlist.name}</h1>
          )}

      <div className="edit-controls">
        <div className="icon-with-text">
          <img 
            src={isEditMode ? "/icons/done_movies.svg" : "/icons/edit_watchlist.svg"} 
            alt="Edit" 
            className="edit-button"
            onClick={handleEditToggle}
          />
          <span className="icon-text">{isEditMode ? "Done" : "Edit"}</span>
        </div>
        
        {isEditMode && (
          <>
            <div className="icon-with-text action-group">
              <img
                src="/icons/move_movies.svg"
                alt="Move movies"
                onMouseEnter={() => setShowMoveDropdown(true)}
                onMouseLeave={() => setShowMoveDropdown(false)}
              />
              <span className="icon-text">Move</span>
              {showMoveDropdown && (
                <div 
                  className="move-dropdown"
                  onMouseEnter={handleMouseEnter} // Dacă mouse-ul intră pe dropdown, anulează timer-ul
                  onMouseLeave={handleMouseLeave} // Dacă mouse-ul iese de pe dropdown, pornește timer-ul
                >
                  {allWatchlists.map(w => (
                    <div
                      key={w.id}
                      className="dropdown-item"
                      onClick={() => handleMoveMovies(w.id)}
                    >
                      <input 
                        type="checkbox" 
                        checked={selectedMovies.length > 0 && w.movies.some(movie => selectedMovies.includes(movie.id))}
                        readOnly
                        className="dropdown-checkbox"
                      />
                      {w.name}
                    </div>
                  ))}
                </div>
              )}
            </div>

            <div className="icon-with-text">
              <img
                src="/icons/remove_movies.svg"
                alt="Remove movies"
                className="remove-button"
                onClick={handleRemoveMovies}
              />
              <span className="icon-text">Remove</span>
            </div>
          </>
        )}
      </div>

        </div>

        <div className="movies-grid">
          {watchlist.movies.length === 0 ? (
            <p className="no-movies">No movies in this watchlist yet. <br/>Search for a movie to add it here!</p>
          ) : (
            watchlist.movies.map(movie => (
              <div 
                key={movie.id}  
                className={`movie-card ${isEditMode ? 'edit-mode' : ''}`}
              >
                {isEditMode && (
                  <input
                    type="checkbox"
                    checked={selectedMovies.includes(movie.id)}
                    onChange={() => handleMovieSelect(movie.id)}
                    className="movie-checkbox"
                  />
                )}
                
                {!isEditMode ? (
                  <Link to={`/movies/${movie.id}`} className="movie-link">
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
                ) : (
                  <div className="movie-info">
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
                    <h4>{movie.title}</h4>
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default WatchlistDetails;
