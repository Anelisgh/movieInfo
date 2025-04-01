INSERT INTO directors (name, birth_date, debut_year, is_active) VALUES
('Christopher Nolan', '1970-07-30', 1998, 1),
('Chris Columbus', '1958-09-10', 1987, 1),
('Peter Jackson', '1961-10-31', 1987, 1),
('Robert Zemeckis', '1952-05-14', 1978, 1),
('James Cameron', '1954-08-16', 1978, 1),
('Gore Verbinski', '1964-03-16', 1996, 1),
('Andrew Adamson', '1966-12-01', 2001, 1),
('Joss Whedon', '1964-06-23', 1997, 1),
('Lana and Lilly Wachowski', '1965-06-21', 1996, 1),
('Frank Capra', '1897-05-18', 1926, 0),
('Steven Spielberg', '1946-12-18', 1969, 1),
('Peter Weir', '1944-08-21', 1974, 1);

INSERT INTO movies (title, description, genre, release_year, director_id, photo_url) VALUES
('Inception', 'A thief who steals corporate secrets through dream-sharing technology is given the task of planting an idea into the mind of a C.E.O.', 'SCI_FI', 2010, 1, 'https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg'),
('Harry Potter and the Philosopher\'s Stone', 'An orphaned boy enrolls in a school of wizardry, where he learns the truth about himself, his family and the terrible evil that haunts the magical world.', 'FANTASY', 2001, 2, 'https://m.media-amazon.com/images/M/MV5BNTU1MzgyMDMtMzBlZS00YzczLThmYWEtMjU3YmFlOWEyMjE1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('The Hobbit: An Unexpected Journey', 'A reluctant Hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home from a dragon.', 'ADVENTURE', 2012, 3, 'https://m.media-amazon.com/images/M/MV5BMTcwNTE4MTUxMl5BMl5BanBnXkFtZTcwMDIyODM4OA@@._V1_FMjpg_UX1000_.jpg'),
('Back to the Future', 'Marty McFly, a 17-year-old high school student, is accidentally sent thirty years into the past in a time-traveling DeLorean.', 'SCI_FI', 1985, 4, 'https://m.media-amazon.com/images/M/MV5BZmM3ZjE0NzctNjBiOC00MDZmLTgzMTUtNGVlOWFlOTNiZDJiXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('Avatar', 'A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.', 'SCI_FI', 2009, 5, 'https://m.media-amazon.com/images/M/MV5BMTc3MDcwMTc1MV5BMl5BanBnXkFtZTcwMzk4NTU3Mg@@._V1_.jpg'),
('Pirates of the Caribbean: The Curse of the Black Pearl', 'Blacksmith Will Turner teams up with eccentric pirate Captain Jack Sparrow to save his love from Jack\'s former pirate allies.', 'ADVENTURE', 2003, 6, 'https://m.media-amazon.com/images/M/MV5BNDhlMzEyNzItMTA5Mi00YWRhLThlNTktYTQyMTA0MDIyNDEyXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('Shrek', 'An ogre and a donkey set off to rescue a princess for a villainous lord in order to get his swamp back.', 'ANIMATION', 2001, 7, 'https://m.media-amazon.com/images/M/MV5BN2FkMTRkNTUtYTI0NC00ZjI4LWI5MzUtMDFmOGY0NmU2OGY1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('The Avengers', 'Earth\'s mightiest heroes must come together and learn to fight as a team to stop the mischievous Loki and his alien army from enslaving humanity.', 'ACTION', 2012, 8, 'https://m.media-amazon.com/images/M/MV5BNGE0YTVjNzUtNzJjOS00NGNlLTgxMzctZTY4YTE1Y2Y1ZTU4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', 'SCI_FI', 1999, 9, 'https://m.media-amazon.com/images/M/MV5BN2NmN2VhMTQtMDNiOS00NDlhLTliMjgtODE2ZTY0ODQyNDRhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('Titanic', 'A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.', 'ROMANCE', 1997, 5, 'https://m.media-amazon.com/images/M/MV5BNjg5OTE2MjEtM2FlZS00Y2UxLWI5ZTItMDhjOThiMzM4N2QyXkEyXkFqcGc@._V1_.jpg'),
('The Lord of the Rings: The Fellowship of the Ring', 'A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.', 'FANTASY', 2001, 3, 'https://m.media-amazon.com/images/M/MV5BNzIxMDQ2YTctNDY4MC00ZTRhLTk4ODQtMTVlOWY4NTdiYmMwXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),
('It\'s a Wonderful Life', 'An angel is sent from Heaven to help a desperately frustrated businessman by showing him what life would have been like if he had never existed.', 'DRAMA', 1946, 10, 'https://m.media-amazon.com/images/M/MV5BMDM4OWFhYjEtNTE5Yy00NjcyLTg5N2UtZDQwNDZlYjlmNDU5XkEyXkFqcGc@._V1_.jpg'),
('Catch Me If You Can', 'A true story about Frank Abagnale Jr., who, before his 19th birthday, successfully conned millions of dollars by posing as a Pan Am pilot, doctor, and legal prosecutor.', 'DRAMA', 2002, 11, 'https://m.media-amazon.com/images/M/MV5BZTZmNzJjYzEtZDY4ZC00YWZlLTg0ZDgtYWRmYjYyZWFhMWFlXkEyXkFqcGc@._V1_.jpg'),
('Dead Poets Society', 'English teacher John Keating inspires his students to look at poetry with a different perspective of authentic knowledge and feelings.', 'DRAMA', 1989, 12, 'https://m.media-amazon.com/images/M/MV5BMDYwNGVlY2ItMWYxMS00YjZiLWE5MTAtYWM5NWQ2ZWJjY2Q3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg');

INSERT INTO actors (name, birth_date, debut_year, is_active) VALUES
('Leonardo DiCaprio', '1974-11-11', 1991, 1),
('Daniel Radcliffe', '1989-07-23', 2001, 1),
('Martin Freeman', '1971-09-08', 1997, 1),
('Michael J. Fox', '1961-06-09', 1976, 0),
('Sam Worthington', '1976-08-02', 2000, 1),
('Johnny Depp', '1963-06-09', 1984, 1),
('Mike Myers', '1963-05-25', 1982, 1),
('Robert Downey Jr.', '1965-04-04', 1970, 1),
('Keanu Reeves', '1964-09-02', 1986, 1),
('Kate Winslet', '1975-10-05', 1991, 1),
('Elijah Wood', '1981-01-28', 1989, 1),
('James Stewart', '1908-05-20', 1935, 0),
('Tom Hanks', '1956-07-09', 1980, 1),
('Robin Williams', '1951-07-21', 1977, 0),
('Emma Watson', '1990-04-15', 2001, 1),
('Orlando Bloom', '1977-01-13', 1997, 1),
('Scarlett Johansson', '1984-11-22', 1994, 1),
('Laurence Fishburne', '1961-07-30', 1972, 1),
('Billy Zane', '1966-02-24', 1985, 1),
('Ian McKellen', '1939-05-25', 1964, 1);

INSERT INTO movie_actor (movie_id, actor_id) VALUES
(1, 1), -- Inception - DiCaprio
(2, 2), -- Harry Potter - Radcliffe
(2, 15), -- Harry Potter - Emma Watson
(3, 3), -- The Hobbit - Freeman
(4, 4), -- Back to the Future - Fox
(5, 5), -- Avatar - Worthington
(6, 6), -- Pirates - Depp
(6, 16), -- Pirates - Bloom
(7, 7), -- Shrek - Myers
(8, 8), -- Avengers - Downey
(8, 17), -- Avengers - Johansson
(9, 9), -- Matrix - Reeves
(9, 18), -- Matrix - Fishburne
(10, 1), -- Titanic - DiCaprio
(10, 10), -- Titanic - Winslet
(10, 19), -- Titanic - Zane
(11, 11), -- LOTR - Wood
(11, 20), -- LOTR - McKellen
(12, 12), -- Wonderful Life - Stewart
(13, 1), -- Catch Me - DiCaprio
(13, 13), -- Catch Me - Hanks
(14, 14); -- Dead Poets - Williams

INSERT INTO users (email, name, password) VALUES
('emma@gmail.com', 'Emma', '$2a$10$2s2hp/MLnSLzEk.pj.XnQeGnnmMZUI0zpEhU0H/j744w70TvUh/L2'), -- 123456
('anelis@gmail.com', 'Admin', '$2a$10$iiF6QKXOqfuQ0bODIMEwhO0fYXm2yrG7Tle/IivLbnVZjFrQZ5dMW'); -- admin
-- user_id=1
INSERT INTO reviews (comment, rating, type, movie_id, user_id) VALUES
('One of the most mind-bending movies I have ever seen. The concept of dream inception is brilliantly executed.', 9.5, 'PUBLIC', 1, 1),
('A magical introduction to the wizarding world. Although targeted for kids, adults can enjoy it too.', 8.0, 'PUBLIC', 2, 1),
('Stunning visuals and groundbreaking special effects, but the story felt somewhat derivative.', 7.5, 'PUBLIC', 5, 1),
('This film redefined sci-fi for a generation. The "red pill or blue pill" scene is iconic.', 9.0, 'PUBLIC', 9, 1),
('A timeless classic that always makes me cry. Cameron perfectly balanced the romance with the historical tragedy.', 8.5, 'PUBLIC', 10, 1);
-- user_id=2
INSERT INTO reviews (comment, rating, type, movie_id, user_id) VALUES
('The movie left me confused initially, but after rewatching it I appreciated the narrative complexity. A visual masterpiece.', 8.7, 'PUBLIC', 1, 2),
('One of the most impressive fantasy adaptations. The special effects remain incredible even after so many years.', 9.2, 'PUBLIC', 11, 2),
('An animation that redefined the genre. The humor works for both children and adults.', 8.5, 'PUBLIC', 7, 2),
('Too many special effects and too little substance. The story is predictable.', 6.3, 'PRIVATE', 8, 2),
('A classic film that moved me deeply. The story and message remain relevant today.', 9.5, 'PUBLIC', 12, 2);

-- 3 Watchlists for User ID 1
INSERT INTO watchlists (name, user_id) VALUES
('Sci-Fi Favorites', 1),
('Must Watch Classics', 1),
('Fantasy Adventures', 1);

-- 10 Watchlist-Movie Relationships
INSERT INTO watchlist_movie (watchlist_id, movie_id) VALUES
(1, 1),  -- Sci-Fi Favorites: Inception
(1, 4),  -- Sci-Fi Favorites: Back to the Future
(1, 5),  -- Sci-Fi Favorites: Avatar
(1, 9),  -- Sci-Fi Favorites: The Matrix
(2, 4),  -- Must Watch Classics: Back to the Future
(2, 10), -- Must Watch Classics: Titanic
(2, 12), -- Must Watch Classics: It's a Wonderful Life
(3, 2),  -- Fantasy Adventures: Harry Potter
(3, 3),  -- Fantasy Adventures: The Hobbit
(3, 11); -- Fantasy Adventures: LOTR

-- 5 Watched Movies for User ID 1
INSERT INTO watched_movies (is_watched, watch_date, movie_id, user_id) VALUES
(1, '2023-12-15', 1, 1),  -- Inception
(1, '2023-11-20', 2, 1),  -- Harry Potter
(1, '2024-01-05', 9, 1),  -- The Matrix
(1, '2024-02-14', 10, 1), -- Titanic (watched on Valentine's Day)
(1, '2024-03-10', 5, 1);  -- Avatar