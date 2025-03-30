import API from './api';

// Obține listele de vizionare ale utilizatorului
export const getWatchlists = async () => {
  try {
    const response = await API.get('/watchlists/user');
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Creează o listă de vizionare nouă
export const createWatchlist = async (watchlistData) => {
  try {
    const response = await API.post('/watchlists/add', watchlistData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Obține recomandări de filme pentru utilizator
export const getRecommendations = async () => {
  try {
    const response = await API.get('/api/movies/recommendations');
    console.log('Recommendations data:', response.data);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const addToWatchlist = (watchlistId, movieId) => {
  return API.post(`/watchlists/${watchlistId}/add-movie/${movieId}`);
};

export const removeFromWatchlist = (watchlistId, movieId) => {
  return API.delete(`/watchlists/${watchlistId}/remove-movie/${movieId}`);
};

export const markAsWatched = (movieId) => {
  return API.post('/watched-movies/mark-as-watched', { movieId });
};

export const checkWatchedStatus = (movieId) => {
  return API.get(`/watched-movies/check/${movieId}`)
      .then(response => response.data);
};

export const checkInWatchlist = (movieId) => {
  return API.get(`/watchlists/contains/${movieId}`)
      .then(response => response.data);
};

export const getWatchlistsWithStatus = async (movieId) => {
  try {
    const response = await API.get(`/watchlists/user-with-status/${movieId}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Șterge o listă de vizionare specifică
export const deleteWatchlist = async (watchlistId) => {
  try {
    const response = await API.delete(`/watchlists/delete/${watchlistId}`);
    return response.status; // Returnăm statusul pentru a confirma ștergerea
  } catch (error) {
    throw error; // Aruncăm eroarea pentru gestionare în alte părți
  }
};

export const getWatchlistById = async (id) => {
  try {
    const response = await API.get(`/watchlists/get/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const moveMoviesToWatchlist = async (sourceId, targetId, movieIds) => {
  try {
    const response = await API.post(`/watchlists/${sourceId}/move-to/${targetId}`, movieIds);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const removeMoviesFromWatchlist = async (watchlistId, movieIds) => {
  try {
    const response = await API.delete(`/watchlists/${watchlistId}/remove-movies`, {
      data: movieIds
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const updateWatchlist = async (watchlistId, watchlistData) => {
  try {
    const response = await API.put(`/watchlists/update/${watchlistId}`, watchlistData);
    return response.data;
  } catch (error) {
    throw error;
  }
};
