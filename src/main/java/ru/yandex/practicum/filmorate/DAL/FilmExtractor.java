package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmExtractor implements ResultSetExtractor<Film> {
    @Override
    public Film extractData(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = null;
        Integer mpaId = rs.getObject("mpa_id", Integer.class);
        if (mpaId != null) {
            mpa = new Mpa();
            mpa.setId(mpaId);
            mpa.setName(rs.getString("mpa_name"));
        }
        film.setMpa(mpa);

        Set<Genre> genres = new HashSet<>();
        do {
            Integer genreId = rs.getObject("genre_id", Integer.class);
            if (genreId != null) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));
                genres.add(genre);
            }
        } while (rs.next());

        film.setGenres(new ArrayList<>(genres.stream()
                .sorted(Comparator.comparingInt(genre -> genre.getId())) // Сортируем по ID жанра
                .collect(Collectors.toList())));


        return film;
    }
}


