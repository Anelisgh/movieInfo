import axios from 'axios';

// Creează instanța Axios
const API = axios.create({
  baseURL: 'http://localhost:8080', // URL-ul backend-ului
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Adaugă un interceptor pentru a atașa token-ul JWT la fiecare request
API.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Adaugă un interceptor pentru a redirecționa utilizatorii neautentificați
API.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token'); // Șterge token-ul expirat
      window.location.href = '/login'; // Redirecționează utilizatorul la login
    }
    return Promise.reject(error);
  }
);

// Funcție pentru obținerea detaliilor unui film
export const getMovieDetails = async (movieId) => {
  try {
    const response = await API.get(`/movies/${movieId}`);
    return response.data;
  } catch (error) {
    throw error;
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
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const searchMovies = async (title) => {
  try {
    const encodedTitle = encodeURIComponent(title);
    const response = await API.get(`/movies/search?title=${encodedTitle}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default API;
