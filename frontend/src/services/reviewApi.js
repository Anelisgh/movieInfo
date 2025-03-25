import API from './api'; // Importă instanța Axios

// Obține review-ul utilizatorului pentru un film specific
export const getUserReviewForMovie = async (movieId) => {
    try {
        const response = await API.get(`/reviews/user/${movieId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching user review:', error);
        throw error;
    }
};

// Adaugă un review nou
// services/reviewApi.js
export const addReview = async (reviewData) => {
    try {
        // Nu mai trimite userId în reviewData
        const response = await API.post('/reviews/add', {
            movieId: reviewData.movieId,
            rating: reviewData.rating,
            comment: reviewData.comment,
            reviewType: reviewData.reviewType
        });
        return response.data;
    } catch (error) {
        console.error('Error adding review:', error);
        throw error;
    }
};

// Actualizează un review existent
export const updateReview = async (reviewId, reviewData) => {
    try {
        const response = await API.put(`/reviews/update/${reviewId}`, reviewData);
        return response.data;
    } catch (error) {
        console.error('Error updating review:', error);
        throw error;
    }
};

// Șterge un review
export const deleteReview = async (reviewId) => {
    try {
        await API.delete(`/reviews/delete/${reviewId}`);
    } catch (error) {
        console.error('Error deleting review:', error);
        throw error;
    }
};