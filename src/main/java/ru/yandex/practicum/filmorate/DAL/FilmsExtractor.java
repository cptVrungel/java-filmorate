package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmsExtractor implements ResultSetExtractor<Collection<Film>> {

    @Override
    public Collection<Film> extractData(ResultSet rs) throws SQLException {
        Map<Integer, Film> films = new LinkedHashMap<>();

        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            Film film = films.get(filmId);

            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));

                // MPA
                Integer mpaId = rs.getObject("mpa_id", Integer.class);
                if (mpaId != null) {
                    Mpa mpa = new Mpa();
                    mpa.setId(mpaId);
                    mpa.setName(rs.getString("mpa_name"));
                    film.setMpa(mpa);
                }

                film.setGenres(new ArrayList<>());
                films.put(filmId, film);
            }

            Integer genreId = rs.getObject("genre_id", Integer.class);
            if (genreId != null) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));

                if (!film.getGenres().contains(genre)) {
                    film.getGenres().add(genre);
                }
            }
        }
        for (Film film : films.values()) {
            film.setGenres(film.getGenres().stream()
                    .sorted(Comparator.comparingInt(genre -> genre.getId())) // Сортируем по ID жанра
                    .collect(Collectors.toList()));
        }

        return films.values();
    }
}

