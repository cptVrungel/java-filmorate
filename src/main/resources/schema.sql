CREATE TABLE IF NOT EXISTS mpas (
    mpa_id INTEGER PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_date TIMESTAMP NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER REFERENCES mpas(mpa_id)
);

-- Таблица жанров
CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Таблица связи между фильмами и жанрами
CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER NOT NULL REFERENCES films(film_id),
    genre_id INTEGER NOT NULL REFERENCES genres(genre_id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(40) NOT NULL,
    email VARCHAR(40) NOT NULL,
    name VARCHAR(40) NOT NULL,
    birthday TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


