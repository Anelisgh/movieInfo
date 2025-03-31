import axios from 'axios';

// Creează instanța Axios
const API = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'https://movieinfo-ftki.onrender.com' // URL-ul backend-ului
  // withCredentials: true,
  // // headers: {
  // //   'Content-Type': 'application/json',
  // // },
});

// Adaugă un interceptor pentru a atașa token-ul JWT la fiecare request
API.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// Interceptor Response
// Actualizează interceptorul de response
API.interceptors.response.use(
  response => response,
  error => {
    const originalRequest = error.config;
    const isPublicEndpoint = [
      '/auth/login',
      '/auth/register',
      '/api/movies/recommendations',
      '/api/movies/search',
      '/api/movies/genres',
      /^\/api\/movies\/\d+$/
    ].some(pattern => {
      if (typeof pattern === 'string') return originalRequest.url.includes(pattern);
      return pattern.test(originalRequest.url);
    });

    if (error.response?.status === 401 && !isPublicEndpoint) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Funcție pentru obținerea detaliilor unui film
export const getMovieDetails = async (movieId) => {
  try {
      const response = await API.get(`/api/movies/${movieId}`);
      return {
          ...response.data,
          // Normalizează datele pentru cazurile unde backend-ul nu trimite valori
          actors: response.data.actors || [],
          reviews: response.data.reviews || [],
          averageRating: response.data.averageRating || 0
      };
  } catch (error) {
      if (error.response?.status === 404) {
          throw new Error('Filmul nu a fost găsit');
      }
      throw new Error('Eroare de server');
  }
};

// Funcție pentru înregistrarea utilizatorului
export const registerUser = async (userData) => {
  try {
    const response = await API.post('/auth/register', userData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Funcție pentru autentificarea utilizatorului
export const loginUser = async (credentials) => {
  try {
    const response = await API.post('/auth/login', credentials);
    if (response.data.token) {
      localStorage.setItem('token', response.data.token); // Salvează token-ul
      API.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`; // Setează header implicit
    }
    return response.data;
  } catch (error) {
    throw error;
  }
};
export const searchMovies = async (title) => {
  try {
    const encodedTitle = encodeURIComponent(title);
    const response = await API.get(`/api/movies/search?title=${encodedTitle}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default API;
