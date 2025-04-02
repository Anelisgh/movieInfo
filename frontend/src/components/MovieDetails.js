import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getMovieDetails } from '../services/api';
import { addToWatchlist, removeFromWatchlist, markAsWatched, checkWatchedStatus, getWatchlistsWithStatus } from '../services/watchlistApi';
import { getUserReviewForMovie, addReview, updateReview, deleteReview } from '../services/reviewApi';
import Navbar from './Navbar';
import '../styles/MovieDetails.css';
import Modal from 'react-modal';

// Înlocuiește importurile relative cu căi absolute
const WatchlistIcon = '/icons/Watchlist.svg';
const WatchedIcon = '/icons/Watched.svg';
const FilledWatchlistIcon = '/icons/FilledWatchlist.svg';
const FilledWatchedIcon = '/icons/FilledWatched.svg';

const MovieDetails = () => {
    const { movieId } = useParams();
    const [movie, setMovie] = useState(null);
    const [isWatched, setIsWatched] = useState(false);
    const [watchlists, setWatchlists] = useState([]);
    const [showDropdown, setShowDropdown] = useState(false);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);
    const [rating, setRating] = useState(0);
    const [hoverRating, setHoverRating] = useState(0);
    const [comment, setComment] = useState('');
    const [reviewType, setReviewType] = useState('PUBLIC');
    const [existingReview, setExistingReview] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();

    // Fetch movie details and initial states
    useEffect(() => {
        if (!movieId || isNaN(Number(movieId))) {
            setErrorMessage('Invalid movie ID');
            setLoading(false);
            return;
        }
    
        const fetchData = async () => {
            try {
                // Pas 1: Încarcă detaliile filmului
                const movieData = await getMovieDetails(movieId);
                setMovie(movieData);
    
                // Pas 2: Verifică autentificarea și încarcă date protejate
                const token = localStorage.getItem('token');
                if (token) {
                    try {
                        const [watchedStatus, userWatchlists] = await Promise.all([
                            checkWatchedStatus(movieId),
                            getWatchlistsWithStatus(movieId)
                        ]);
    
                        setIsWatched(watchedStatus);
                        setWatchlists(userWatchlists || []);
    
                        // Apelează funcția separată pentru review-uri utilizator
                        const userReview = await getUserReviewForMovie(movieId);
                        if (userReview && userReview.id) {
                            setExistingReview(userReview);
                            setRating(userReview.rating);
                            setComment(userReview.comment);
                            setReviewType(userReview.reviewType);
                        } else {
                            setExistingReview(null);}
                    } catch (protectedError) {
                        console.error('Error fetching protected data:', protectedError);
                    }
                }
            } catch (mainError) {
                console.error('Error fetching movie details:', mainError);
                setErrorMessage(mainError.response?.status === 404
                    ? 'Filmul nu a fost găsit'
                    : 'Eroare la încărcarea detaliilor');
            } finally {
                setLoading(false);
            }
        };
    
        fetchData();
    }, [movieId]);
    

    // Gestionează adăugarea/ștergerea din watchlist
    const handleWatchlistToggle = async (watchlistId, containsMovie) => {
        if (!localStorage.getItem('token')) {
            navigate('/login');
            return;
        }
        try {
            if (containsMovie) {
                await removeFromWatchlist(watchlistId, movieId);
            } else {
                await addToWatchlist(watchlistId, movieId);
            }
            // Actualizează starea locală
            setWatchlists(watchlists.map(watchlist =>
                watchlist.id === watchlistId
                    ? { ...watchlist, containsMovie: !watchlist.containsMovie }
                    : watchlist
            ));
        } catch (error) {
            console.error('Error toggling watchlist:', error);
        }
    };

    const handleUserReview = async (movieId) => {
        try {
            const userReview = await getUserReviewForMovie(movieId);
            console.log('userReview:', userReview);
    
            if (userReview && userReview.id) {
                setExistingReview(userReview);
                setRating(userReview.rating || 0);
                setComment(userReview.comment || '');
                setReviewType(userReview.reviewType || 'PUBLIC');
            } else {
                setExistingReview(null);
              }
        } catch (error) {
            console.error('Error fetching user review:', error);
        }
    };

    // Gestionează marcarea ca "watched"
    const handleWatchedToggle = async () => {
        if (!localStorage.getItem('token')) {
            navigate('/login');
            return;
        }
        try {
            await markAsWatched(movieId);
            setIsWatched(!isWatched); // Comută starea locală
        } catch (error) {
            console.error('Error toggling watched status:', error);
        }
    };

    // Gestionează trimiterea review-ului
    const handleSubmit = async (selectedReviewType) => {
        // Verifică dacă utilizatorul este autentificat
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }
    
        const finalReviewType = selectedReviewType || reviewType;
        const reviewData = {
            movieId,
            rating,
            comment,
            reviewType: finalReviewType
        };
        
        try {
            if (existingReview?.id) {
                await updateReview(existingReview.id, reviewData);
            } else {
                await addReview(reviewData);
            }
            await handleUserReview(movieId);
            setIsEditing(false);
            setReviewType(finalReviewType);
            window.location.reload();
        } catch (error) {
            console.error('Error submitting review:', error);
            if (error.response?.status === 401 || error.response?.status === 403) {
                localStorage.removeItem('token');
                navigate('/login');
            }
        }
    };

    // Gestionează ștergerea review-ului
    const handleDelete = async () => {
        try {
            await deleteReview(existingReview.id);
            window.location.reload();
        } catch (error) {
            console.error('Error deleting review:', error);
        }
    };

    

    if (loading) return <div>Loading...</div>;
    if (errorMessage) return <div className="error">{errorMessage}</div>;
    if (!movie) return <div>Movie not found</div>;

    return (
        <div className="main-content movie-details-page">
            <Navbar />
            <div className="movie-details-container">
                <div className="movie-poster">
                    <img 
                        src={movie.photoUrl || '/placeholder.jpg'} 
                        alt={movie.title || 'Movie poster'}
                        onError={(e) => { e.target.src = '/placeholder.jpg'; }}
                    />
                </div>
                
                <div className="movie-info">
                    <div className="movie-header">
                        <div className="movie-title">
                            <h5>{movie.title}</h5>
                            <p className="release-year">({movie.releaseYear})</p>
                        </div>

                        <div className="movie-actions">
                            {/* Iconița pentru Watchlist */}
                            <div 
                                className="action-icon" 
                                onMouseEnter={() => setShowDropdown(true)} 
                                onMouseLeave={() => setShowDropdown(false)}
                            >
                                <img src={Array.isArray(watchlists) && watchlists.some(w => w?.containsMovie) ? FilledWatchlistIcon : WatchlistIcon} alt="Watchlist"/>
                                {showDropdown && (
                                    <div className="watchlist-dropdown"
                                    onMouseEnter={() => setShowDropdown(true)}
                                    onMouseLeave={() => setShowDropdown(false)}>
                                        {Array.isArray(watchlists) && watchlists.length > 0 ? (
      watchlists.map(watchlist => (
                                            <div 
                                                key={watchlist.id}
                                                className="watchlist-item"
                                                onClick={() => handleWatchlistToggle(watchlist.id, watchlist.containsMovie)}
                                            >
                                                <input 
                                                    type="checkbox" 
                                                    checked={watchlist?.containsMovie || false}  
                                                    readOnly
                                                />
                                                {watchlist.name}
                                            </div>
                                        ))
                                    ) : (
                                      <div className="empty-watchlist-message">
                                        No watchlists available
                                      </div>
                                    )}
                                  </div>
                                )}
                            </div>

                            {/* Iconița pentru Watched */}
                            <div className="action-icon" onClick={handleWatchedToggle}>
                                <img 
                                    src={isWatched ? FilledWatchedIcon : WatchedIcon} 
                                    alt={isWatched ? "Mark as unwatched" : "Mark as watched"} 
                                />
                            </div>
                        </div>
                    </div>

                    <div className="movie-genre">{movie.genre.toLowerCase()}</div>

                    <div className="movie-section">
                        <p><strong>Description: </strong>{movie.description}</p>
                    </div>

                    <div className="movie-section">
                        <p><strong>Director: </strong>{movie.directorName}</p>
                    </div>

                    <div className="movie-section">
                        <p>
                            <strong>Cast: </strong> 
                            {(movie.actors || []).map((actor, index) => (
                                <span key={actor.id || actor}>
                                    {actor}{index < movie.actors.length - 1 && ', '}
                                </span>
                            ))}
                        </p>
                    </div>

                    {/* Secțiunea de review */}
                <div className="add-a-review-container">
                    <div className="title-add-review">
                        <img 
                            src='/icons/add-review.svg'
                            alt="Review" 
                            className="add-review-img"
                        />
                        <p>{(existingReview && !isEditing) ? 'Your review' : 'Add a review'}</p>
                    </div>

                    {/* Stele de rating */}
                    <div className="star-rating">
                        {[...Array(10)].map((_, index) => {
                            const ratingValue = index + 1;
                            return (
                                <img
                                    key={index}
                                    src={ratingValue <= (hoverRating || rating) ? '/icons/full_star.svg' : '/icons/empty_star.svg'}
                                    alt="Star"
                                    className="star"
                                    onMouseEnter={() => (!existingReview || isEditing) && setHoverRating(ratingValue)}
                                    onMouseLeave={() => (!existingReview || isEditing) && setHoverRating(0)}
                                    onClick={() => (!existingReview || isEditing) && setRating(ratingValue)}
                                />
                            );
                        })}
                    </div>
                    <div className='review-container'>
                        {/* Casetă comentariu */}
                        <textarea
                            className="review-textarea"
                            placeholder="Write your review..."
                            value={comment}
                            onChange={(e) => (!existingReview || isEditing) && setComment(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter' && !e.shiftKey) {
                                    e.preventDefault();
                                    handleSubmit('PUBLIC');
                                }
                            }}
                            disabled={!!existingReview && !isEditing}
                        />
                        
                        {/* Butoane acțiune */}
                        <div className="review-actions">
                        {existingReview?.id ? ( 
                            isEditing ? (
                            <>
                                <button 
                                className={`type-btn ${reviewType === 'PUBLIC' ? 'active' : ''}`}
                                onClick={() => handleSubmit('PUBLIC')}
                                >
                                🌐 Public
                                </button>
                                <button 
                                className={`type-btn ${reviewType === 'PRIVATE' ? 'active' : ''}`}
                                onClick={() => handleSubmit('PRIVATE')}
                                >
                                👁️ Private
                                </button>
                            </>
                            ) : (
                            <>
                                <button 
                                className="edit-btn" 
                                onClick={() => {setIsEditing(true);
                                    setReviewType(existingReview.reviewType); 
                                }}
                                >
                                <img src="/icons/edit.svg" alt="Edit"/> Edit
                                </button>
                                <button 
                                className="delete-btn" 
                                onClick={() => setShowDeleteModal(true)}
                                >
                                <img src="/icons/delete.svg" alt="Delete"/> Delete
                                </button>
                            </>
                            )
                        ) : (
                                localStorage.getItem('token') ? (
                                    <>
                                        <button className={`type-btn ${reviewType === 'PUBLIC' ? 'active' : ''}`}
                                            onClick={() => handleSubmit('PUBLIC')}>
                                            🌐 Public
                                        </button>
                                        <button className={`type-btn ${reviewType === 'PRIVATE' ? 'active' : ''}`}
                                            onClick={() => handleSubmit('PRIVATE')}>
                                            👁️ Private
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <button className="type-btn" onClick={() => navigate('/login')}>
                                            🌐 Public
                                        </button>
                                        <button className="type-btn" onClick={() => navigate('/login')}>
                                            👁️ Private
                                        </button>
                                    </>
                                )
                            )}
                        </div>
                    </div>
                </div>
                </div>
            </div>

            {/* Recenziile filmului */}
            <div className="public-reviews-container">
                <div className="movie-section public-review">
                    <h3>Reviews</h3>
                    {movie.reviews?.length > 0 ? (
                        <div className="reviews-container">
                            {(movie.reviews || []).map((review) => (
                                <div key={review.id || `${review.username}-${review.comment}`} className="review-card">
                                    <div className="review-header">
                                        <span className="review-username">{review.username}</span>
                                        <div className="rating-container">
                                            <img src="/icons/full_star.png" alt="Full Yellow Star" className="star-img" />
                                            <span className="review-rating">{review.rating}/10</span>
                                        </div>
                                    </div>
                                    <p className="review-text">{review.comment}</p>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No reviews yet</p>
                    )}
                </div>
                <div className="average-rating-container">
                <div className="average-rating">
                    <img src="\icons\avg_rating.svg" alt="Star" className="star-icon" />
                    <span>{movie.averageRating.toFixed(1)}</span>
                </div>
            </div>
            </div>

            {/* Modal confirmare ștergere */}
            <Modal
                isOpen={showDeleteModal}
                onRequestClose={() => setShowDeleteModal(false)}
                className="delete-modal"
                overlayClassName="delete-overlay"
                ariaHideApp={false}
            >
                <h3>Are you sure you want to delete your review?</h3>
                <div className="modal-buttons">
                    <button className="cancel-btn" onClick={() => setShowDeleteModal(false)}>Cancel</button>
                    <button className="confirm-delete-btn" onClick={handleDelete}>Delete</button>
                </div>
            </Modal>
        </div>
    );
};

export default MovieDetails;