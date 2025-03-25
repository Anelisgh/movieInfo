import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
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

    // Fetch movie details and initial states
    useEffect(() => {
        if (!movieId || isNaN(Number(movieId))) {
            setErrorMessage('Invalid movie ID');
            setLoading(false);
            return;
        }
    
        const fetchData = async () => {
            try {
                const data = await getMovieDetails(movieId);
                setMovie(data);
    
                const watchedStatus = await checkWatchedStatus(movieId);
                setIsWatched(watchedStatus);
    
                const userWatchlists = await getWatchlistsWithStatus(movieId);
                setWatchlists(userWatchlists);
    
                await handleUserReview(movieId); // Apelează funcția separată
            } catch (error) {
                console.error('Error fetching data:', error);
                setErrorMessage('Failed to load movie details');
            } finally {
                setLoading(false);
            }
        };
    
        fetchData();
    }, [movieId]);

    // Gestionează adăugarea/ștergerea din watchlist
    const handleWatchlistToggle = async (watchlistId, containsMovie) => {
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
    
            if (userReview && userReview.rating !== undefined) {
                setExistingReview(userReview);
                setRating(userReview.rating || 0);
                setComment(userReview.comment || '');
                setReviewType(userReview.reviewType || 'PUBLIC');
            }
        } catch (error) {
            console.error('Error fetching user review:', error);
        }
    };

    // Gestionează marcarea ca "watched"
    const handleWatchedToggle = async () => {
        try {
            await markAsWatched(movieId);
            setIsWatched(!isWatched); // Comută starea locală
        } catch (error) {
            console.error('Error toggling watched status:', error);
        }
    };

    // Gestionează trimiterea review-ului
    const handleSubmit = async (selectedReviewType) => {
        const finalReviewType = selectedReviewType || reviewType;
        const reviewData = {
            movieId,
            rating,
            comment,
            reviewType: finalReviewType // Prioritizează valoarea de la buton
        };
        try {
            if (existingReview) {
                await updateReview(existingReview.id, reviewData);
            } else {
                await addReview(reviewData);
            }
            await handleUserReview(movieId);
            setIsEditing(false);
            setReviewType(finalReviewType);
        } catch (error) {
            console.error('Error submitting review:', error);
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
                                <img 
                                    src={watchlists.some(w => w.containsMovie) ? FilledWatchlistIcon : WatchlistIcon} 
                                    alt="Watchlist" 
                                />
                                {showDropdown && (
                                    <div className="watchlist-dropdown"
                                    onMouseEnter={() => setShowDropdown(true)}
                                    onMouseLeave={() => setShowDropdown(false)}>
                                        {watchlists.map(watchlist => (
                                            <div 
                                                key={watchlist.id}
                                                className="watchlist-item"
                                                onClick={() => handleWatchlistToggle(watchlist.id, watchlist.containsMovie)}
                                            >
                                                <input 
                                                    type="checkbox" 
                                                    checked={watchlist.containsMovie} 
                                                    readOnly
                                                />
                                                {watchlist.name}
                                            </div>
                                        ))}
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
                            {movie.actors?.map((actor, index) => (
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
                                if (e.key === 'Enter' && !e.shiftKey) { // Verifică apăsarea Enter fără Shift
                                    e.preventDefault(); // Previne comportamentul implicit (salt linie nouă)
                                    handleSubmit('PUBLIC'); // Trimitere automată ca "Public"
                                }
                            }}
                            disabled={!!existingReview && !isEditing}
                        />
                        
                        {/* Butoane acțiune */}
                        <div className="review-actions">
                        {(isEditing || !existingReview) ? (
                        <>
                            <button 
                                className={`type-btn ${reviewType === 'PUBLIC' ? 'active' : ''}`}
                                onClick={() => {
                                    setReviewType('PUBLIC');
                                    handleSubmit('PUBLIC');
                                }}
                            >
                                🌐 Public
                            </button>
                            <button
                                className={`type-btn ${reviewType === 'PRIVATE' ? 'active' : ''}`}
                                onClick={() => {
                                    setReviewType('PRIVATE');
                                    handleSubmit('PRIVATE');
                                }}
                            >
                                👁️ Private
                            </button>
                        </>
                    ) : (
                                <>
                                    <button 
                                        className="edit-btn" 
                                        onClick={() => {
                                            setIsEditing(true);
                                            setReviewType(existingReview.reviewType); // Resetează reviewType la valoarea originală
                                        }}
                                    >
                                        <img src="/icons/edit.svg" alt="Edit" />
                                        Edit</button>
                                    <button className="delete-btn" onClick={() => setShowDeleteModal(true)}>
                                        <img src="/icons/delete.svg" alt="Delete" />
                                        Delete</button>
                                </>
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
                            {movie.reviews.map((review) => (
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