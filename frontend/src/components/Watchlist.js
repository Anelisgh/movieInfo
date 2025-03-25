import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getWatchlists, createWatchlist, deleteWatchlist } from '../services/watchlistApi'; // Adăugăm funcția createWatchlist
import Navbar from './Navbar';
import '../styles/Watchlist.css';

const Watchlist = () => {
  const [watchlists, setWatchlists] = useState([]);
  const [showModal, setShowModal] = useState(false); // Stare pentru afișarea modalului
  const [newListName, setNewListName] = useState(''); // Stare pentru numele noii liste
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedLists, setSelectedLists] = useState([]);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  const fetchWatchlists = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      window.location.href = '/login';
      return;
    }

    try {
      const watchlistsData = await getWatchlists();
      setWatchlists(watchlistsData);
    } catch (error) {
      console.error("Error fetching watchlists:", error);
      if (error.response?.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
    }
  };

  const handleListSelection = (listId) => {
    setSelectedLists((prev) =>
      prev.includes(listId)
        ? prev.filter((id) => id !== listId)
        : [...prev, listId]
    );
  };

  const handleDeleteConfirmed = async () => {
    try {
      await Promise.all(selectedLists.map((id) => deleteWatchlist(id)));
      await fetchWatchlists();
      setDeleteMode(false);
      setSelectedLists([]);
    } catch (error) {
      console.error('Error deleting watchlists:', error);
      alert('Failed to delete some lists. Please try again.');
    } finally {
      setShowDeleteConfirm(false);
    }
  };

  const handleDeleteClick = () => {
    if (deleteMode && selectedLists.length > 0) {
      setShowDeleteConfirm(true);
    } else {
      setDeleteMode(!deleteMode);
      setSelectedLists([]);
    }
  };

  const handleCreateList = async () => {
    if (!newListName.trim()) {
      alert('Please enter a name for the list.');
      return;
    }

    try {
      const response = await createWatchlist({ name: newListName, movieIds: [] }); // Trimitem cererea către backend
      if (response) {
        setShowModal(false); // Închidem modalul
        setNewListName(''); // Resetăm câmpul de input
        fetchWatchlists(); // Reîmprospătăm lista de watchlist-uri
      }
    } catch (error) {
      console.error('Error creating watchlist:', error);
      alert('Failed to create the list. Please try again.');
    }
  };

  useEffect(() => {
    fetchWatchlists();
  }, []);

  return (
    <div className='main-content watchlist-page'>
      <Navbar />
      <div className='content'>
      <h2>Watchlists</h2>
        <div className="watchlist-header">    
          <div className="header-buttons">
            <button className="create-list-button" onClick={() => setShowModal(true)}>
              <img src="icons/create_watchlist.svg" alt="create-watchlist" />
              Create a new list
            </button>
          </div>
          <div className="header-right">
      {deleteMode && (
        <span className="selected-count">{selectedLists.length} selected</span>
      )}
      <button
        className={`delete-mode-button ${deleteMode ? 'active' : ''}`}
        onClick={handleDeleteClick}
        >
        {!deleteMode && (
            <img 
            src="/icons/delete_watchlist.svg" 
            alt="Delete Icon" 
            className="w-6 h-6 mr-2"
            />
        )}
        {deleteMode
            ? selectedLists.length > 0 
            ? 'Delete Selected' 
            : 'Cancel'
            : 'Delete Lists'}
        </button>
    </div>
        </div>

        {showDeleteConfirm && (
          <div className="modal-overlay">
            <div className="modal-content">
              <h3>Delete Confirmation</h3>
              <p>Are you sure you want to delete {selectedLists.length} lists?</p>
              <div className="modal-buttons">
                <button onClick={() => setShowDeleteConfirm(false)}>Cancel</button>
                <button onClick={handleDeleteConfirmed}>Delete</button>
              </div>
            </div>
          </div>
        )}

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Create a new list</h3>
            <input
              type="text"
              placeholder="Enter list name"
              value={newListName}
              onChange={(e) => setNewListName(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  handleCreateList(); // Apelăm direct funcția de salvare
                }
              }}
            />
            <div className="modal-buttons">
              <button onClick={() => setShowModal(false)}>Cancel</button>
              <button onClick={handleCreateList}>Save</button>
            </div>
          </div>
        </div>
      )}


        <div className="watchlists-grid">
        {watchlists.map((watchlist) => (
            <Link
            to={`/watchlist/${watchlist.id}`}
            key={watchlist.id}
            className={`watchlist-link ${deleteMode ? 'delete-mode' : ''} ${
                selectedLists.includes(watchlist.id) ? 'selected' : ''
            }`}
            >
            <div
                className={`watchlist-card`}
                onClick={(e) => {
                if (deleteMode) {
                    e.preventDefault(); // Previi navigarea dacă ești în modul delete
                    handleListSelection(watchlist.id);
                }
                }}
            >
              {deleteMode && (
                <div className="selection-checkbox">
                  <input
                    type="checkbox"
                    checked={selectedLists.includes(watchlist.id)}
                    readOnly
                  />
                </div>
              )}

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
                          e.target.src = '/placeholder.jpg';
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
      </div>
    </div>
  );
};

export default Watchlist;
