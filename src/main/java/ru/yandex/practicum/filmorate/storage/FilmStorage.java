package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    void addFilm(Film film);

    boolean checkFilm(Integer id);

    //public Film updateFilm(Film newFilm);
    Collection<Film> getAllFilms();

    Film getFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Set<Integer> getFilmLikes(Integer filmId);

    Collection<Film> getMostLikedFilms(Integer count);
}
