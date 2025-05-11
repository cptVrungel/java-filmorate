package ru.yandex.practicum.filmorate.DAL;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorage {

    //фильмы
    private static final String FIND_ALL_FILMS = "SELECT f.film_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.mpa_id, " +
            "m.name AS mpa_name, " +
            "fg.genre_id, " +
            "g.name AS genre_name " +
            "FROM films f " +
            "LEFT JOIN mpas m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.genre_id";
    private static final String FIND_FILM_BY_ID = "SELECT f.*, m.mpa_id, m.name AS mpa_name, g.genre_id, g.name AS genre_name " +
            "FROM films f " +
            "LEFT JOIN mpas AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
            "WHERE f.film_id = ?";
    private static final String INSERT_FILM = "INSERT INTO films(name, description, release_date, duration) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET name = ?, mpa_id = ?, " +
            "description = ?, release_date = ?, duration = ? WHERE film_id = ?";
    //жанры
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_ALL_GENRES = "SELECT * FROM genres";
    private static final String FIND_GENRES_BY_FILM_ID =
            "SELECT g.* " +
                    "FROM genres AS g " +
                    "JOIN film_genres AS fg ON g.genre_id = fg.genre_id " +
                    "WHERE fg.film_id = ?";
    private static final String INSERT_FILM_GENRES =
            "MERGE INTO film_genres(film_id, genre_id) " +
                    "KEY(film_id, genre_id) " +
                    "VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id = ?";
    //Mpa(рейтинг)
    private static final String FIND_MPA_BY_ID = "SELECT * FROM mpas WHERE mpa_id = ?";
    private static final String FIND_ALL_MPAS = "SELECT * FROM mpas";
    private static final String INSERT_FILM_MPA = "UPDATE films SET mpa_id = ? WHERE film_id = ?";
    //лайки
    private static final String GET_FILM_LIKE = "SELECT user_id from LIKES WHERE film_id = ?";
    private static final String ADD_LIKE = "MERGE INTO LIKES(film_id, user_id) KEY(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";

    private static final String GET_MOST_LIKED_FILMS =
            "SELECT f.film_id, " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id, " +
                    "g.name AS genre_name, " +
                    "COUNT(DISTINCT l.user_id) AS like_count " +
                    "FROM films f " +
                    "LEFT JOIN mpas m ON f.mpa_id = m.mpa_id " +
                    "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                    "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN likes l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name, fg.genre_id, g.name " +
                    "ORDER BY like_count DESC " +
                    "LIMIT ?";
    protected final JdbcTemplate jdbc;
    protected final ResultSetExtractor<Film> filmExtractor;
    protected final ResultSetExtractor<Collection<Film>> filmsExtractor;

    public FilmDbStorage(JdbcTemplate jdbc, FilmExtractor filmExtractor, FilmsExtractor filmsExtractor) {
        this.jdbc = jdbc;
        this.filmExtractor = filmExtractor;
        this.filmsExtractor = filmsExtractor;
    }

    @Override
    public boolean checkFilm(Integer id) {
        try {
            Film film = jdbc.query(FIND_FILM_BY_ID, filmExtractor, id);
            return true;
        } catch (EmptyResultDataAccessException ignored) {
            return false;
        }
    }

    @Override
    public Film getFilmById(Integer id) {
        return jdbc.query(FIND_FILM_BY_ID, filmExtractor, id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        System.out.println("lalala");
        return jdbc.query(FIND_ALL_FILMS, filmsExtractor);
    }

    @Override
    public void addFilm(Film newFilm) {
        save(newFilm);
    }

    @Override
    public void updateFilm(Film newFilm) {
        int rowsUpdated = jdbc.update(
                UPDATE_FILM,
                newFilm.getName(),
                newFilm.getMpa().getId(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getId());
        jdbc.update(DELETE_FILM_GENRES, newFilm.getId());
        insertFilmGenres(newFilm.getId(), newFilm.getGenres());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public Collection<Integer> getFilmLikes(Integer filmId) {
        return jdbc.query(GET_FILM_LIKE, new Object[]{filmId}, (rs, rowNum) -> rs.getInt("user_id"));
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbc.update(ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbc.update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public Collection<Film> getMostLikedFilms(Integer count) {
        return jdbc.query(GET_MOST_LIKED_FILMS, filmsExtractor, count);
    }

    private void save(Film newFilm) {
        System.out.println("Saving film: " + newFilm.getName());
        int id = insert(
                INSERT_FILM,
                newFilm.getName(),
                //newFilm.getMpa().getId(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration()
        );
        newFilm.setId(id);
        insertFilmMpa(newFilm.getId(), newFilm.getMpa());
        insertFilmGenres(id, newFilm.getGenres());
    }

    private int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    public Genre getGenreById(Integer genreId) {
        return jdbc.queryForObject(FIND_GENRE_BY_ID, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName((rs.getString("name")));
            return genre;
        }, genreId);
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        return jdbc.query(FIND_GENRES_BY_FILM_ID, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName((rs.getString("name")));
            return genre;
        }, filmId);
    }

    public Collection<Genre> getAllGenres() {
        return jdbc.query(FIND_ALL_GENRES, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        });
    }

    public void insertFilmGenres(Integer filmId, List<Genre> genres) {
        if (genres != null) {
            for (Genre genre : genres) {
                jdbc.update(INSERT_FILM_GENRES, filmId, genre.getId());
            }
        }
    }

    public Mpa getMpaById(Integer mpaId) {
        return jdbc.queryForObject(FIND_MPA_BY_ID, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName((rs.getString("name")));
            return mpa;
        }, mpaId);
    }

    public Collection<Mpa> getAllMpas() {
        return jdbc.query(FIND_ALL_MPAS, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName((rs.getString("name")));
            return mpa;
        });
    }

    public void insertFilmMpa(Integer filmId, Mpa mpa) {
        if (mpa != null) {
            jdbc.update(INSERT_FILM_MPA, mpa.getId(), filmId);
        }
    }
}
