package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {
    void addFilm(Film film);

    boolean checkFilm(Integer id);

    //public Film updateFilm(Film newFilm);
    Collection<Film> getAllFilms();

    Film getFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Collection<Integer> getFilmLikes(Integer filmId);

    void updateFilm(Film newFilm);

    Collection<Film> getMostLikedFilms(Integer count);

    Genre getGenreById(Integer genreId);

    Collection<Genre> getAllGenres();

    Mpa getMpaById(Integer mpaId);

    Collection<Mpa> getAllMpas();

    Collection<Genre> getFilmGenres(Integer filmId);
}
