import React, { useState, useEffect } from 'react';
import API from '../services/api';
import Navbar from './Navbar';
import '../styles/AddMovie.css';

const AddMovie = () => {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        releaseYear: '',
        genre: '',
        director: '',
        actors: [''],
        photoUrl: ''
    });
    const [genres, setGenres] = useState([]);
    const [errors, setErrors] = useState({});
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const response = await API.get('/movies/genres');
                const sortedGenres = response.data.sort();
                // Transformă textul în lowercase sa-l facem capitalize
                const lowerCaseGenres = sortedGenres.map(genre => genre.toLowerCase());
                setGenres(lowerCaseGenres);
            } catch (error) {
                console.error('Error fetching genres:', error);
            }
        };

        fetchGenres();
    }, []);

    const handleInputChange = (index, value) => {
        const newActors = [...formData.actors];
        newActors[index] = value;
        setFormData({...formData, actors: newActors});
    };

    const addActorField = () => {
        setFormData({...formData, actors: [...formData.actors, '']});
    };

    const removeActorField = (index) => {
        if(formData.actors.length === 1) return;
        const newActors = formData.actors.filter((_, i) => i !== index);
        setFormData({...formData, actors: newActors});
    };

    const validateForm = () => {
        const newErrors = {};
        if(!formData.title) newErrors.title = 'Required';
        if(!formData.director) newErrors.director = 'Required';
        if(!formData.releaseYear || formData.releaseYear < 1880 || formData.releaseYear > 2030) {
            newErrors.releaseYear = 'Invalid release year (1880-2030)';
        }
        if(!formData.genre) newErrors.genre = 'Required';
        if(formData.actors.some(actor => !actor.trim())) {
            newErrors.actors = 'All actors must be filled';
        }
        if (!formData.photoUrl) newErrors.photoUrl = 'Required';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;
    
        try {
            const movieData = {
                ...formData,
                actors: formData.actors.filter(actor => actor.trim())
            };
    
            await API.post('/movies/add', movieData);
            setSuccessMessage('The movie has been added successfully!');
            setErrorMessage('');
            // Reset the form
            setFormData({
                title: '',
                description: '',
                releaseYear: '',
                genre: '',
                director: '',
                actors: [''],
                photoUrl: ''
            });
        } catch (error) {
            console.error('Error adding movie:', error);
            setErrorMessage(error.response?.data?.message || 'Failed to add movie');
            setSuccessMessage('');
        }
    };    

    return (
        <div className="main-content add-movie-page">
            <Navbar />
            <div className="add-movie-container">
                <div className="glass-effect add-movie-form">
                    <div className="form-title">
                    <img src="\icons\popcorn.png" alt="Add a movie"></img><h2>Add a New Movie</h2>
                    </div>
                    {successMessage && <div className="success-message">{successMessage}</div>}
                    {errorMessage && <div className="error-message">{errorMessage}</div>}
                    <form onSubmit={handleSubmit} className="movie-form">
                        <div className="form-group">
                            <label className="form-label">Title:</label>
                            <input
                                type="text"
                                value={formData.title}
                                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                                className={`glass-input ${errors.title ? 'error-border' : ''}`}
                            />
                            {errors.title && <span className="error">{errors.title}</span>}
                        </div>
    
                        <div className="form-group">
                            <label className="form-label">Description:</label>
                            <textarea
                                value={formData.description}
                                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                                className={`glass-input ${errors.title ? 'error-border' : ''}`}
                            />
                        </div>
    
                        {/* Înlocuiește div-ul cu clasa form-row cu un div simplu */}
<div>
    <div className="form-group">
        <label className="form-label">Release Year:</label>
        <input
            type="number"
            value={formData.releaseYear}
            onChange={(e) => setFormData({ ...formData, releaseYear: e.target.value })}
            className={`glass-input ${errors.title ? 'error-border' : ''}`}
        />
        {errors.releaseYear && <span className="error">{errors.releaseYear}</span>}
    </div>

    <div className="form-group">
        <label className="form-label">Genre:</label>
        <select
            value={formData.genre}
            onChange={(e) => setFormData({ ...formData, genre: e.target.value.toUpperCase() })}
            className={`glass-input ${errors.title ? 'error-border' : ''}`}
        >
            <option value="">Select Genre</option>
            {genres.map(genre => (
                <option key={genre} value={genre}>{genre}</option>
            ))}
        </select>
        {errors.genre && <span className="error">{errors.genre}</span>}
    </div>
</div>
    
                        <div className="form-group">
                            <label className="form-label">Director:</label>
                            <input
                                type="text"
                                value={formData.director}
                                onChange={(e) => setFormData({ ...formData, director: e.target.value })}
                                className={`glass-input ${errors.title ? 'error-border' : ''}`}
                            />
                            {errors.director && <span className="error">{errors.director}</span>}
                        </div>
    
                        <div className="form-group">
                            <label className="form-label">Actors:</label>
                            {formData.actors.map((actor, index) => (
                                <div key={index} className="actor-input form-group">
                                    <input
                                        type="text"
                                        value={actor}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                        className={`glass-input ${errors.title ? 'error-border' : ''}`}
                                    />
                                    {index > 0 && (
                                        <button
                                            type="button"
                                            className="remove-actor"
                                            onClick={() => removeActorField(index)}
                                        >
                                            -
                                        </button>
                                    )}
                                    {index === formData.actors.length - 1 && (
                                        <button
                                            type="button"
                                            className="add-actor"
                                            onClick={addActorField}
                                        >
                                            +
                                        </button>
                                    )}
                                </div>
                            ))}
                            {errors.actors && <span className="error">{errors.actors}</span>}
                        </div>
    
                        <div className="form-group">
                            <label className="form-label">Poster URL:</label>
                            <input
                                type="text"
                                value={formData.photoUrl}
                                onChange={(e) => setFormData({ ...formData, photoUrl: e.target.value })}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        handleSubmit(e); // Apelează funcția de submit
                                    }
                                }}
                                className={`glass-input ${errors.title ? "error-border" : ""}`}
                            />
                            {errors.photoUrl && <span className="error">{errors.photoUrl}</span>}
                        </div>

    
                        <button type="submit" className="submit-btn">Create</button>
                    </form>
                </div>
            </div>
        </div>
    );
    
    
};

export default AddMovie;