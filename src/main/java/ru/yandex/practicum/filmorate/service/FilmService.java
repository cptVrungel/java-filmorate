package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        log.info("Фильм успешно добавлен с ID {}", film.getId());
        return film;
    }

    public Film updateFilm(Film newFilm) {
        if (filmStorage.checkFilm(newFilm.getId())) {
            Film oldfilm = filmStorage.getFilmById(newFilm.getId());
            oldfilm.setName(newFilm.getName());
            oldfilm.setDescription(newFilm.getDescription());
            oldfilm.setReleaseDate(newFilm.getReleaseDate());
            oldfilm.setDuration(newFilm.getDuration());
            log.info("Информация о фильме с ID {} успешно обновлена", oldfilm.getId());
            return oldfilm;
        } else {
            throw new NotFoundException("Фильма с таким ID нет !");
        }
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Set<Integer> addLike(Integer filmId, Integer userId) {
        if (!filmStorage.checkFilm(filmId)) {
            throw new NotFoundException("Фильма с ID " + filmId + " нет !");
        }
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с ID " + userId + " нет !");
        }
        if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("ID фильма и пользователя не может быть меньше 0 !");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с ID {} успешно лайкнул фильм с ID {}", userId, filmId);
        return filmStorage.getFilmLikes(filmId);
    }

    public Set<Integer> deleteLike(Integer filmId, Integer userId) {
        if (!filmStorage.checkFilm(filmId)) {
            throw new NotFoundException("Фильма с ID " + filmId + " нет !");
        }
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с ID " + userId + " нет !");
        }
        if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("ID фильма и пользователя не может быть меньше 0 !");
        }
        filmStorage.deleteLike(filmId, userId);
        log.info("Удален лайк пользователя с ID {} фильму с ID {}", userId, filmId);
        return filmStorage.getFilmLikes(filmId);
    }

    public Collection<Film> getMostLikedFilms(Integer count) {
        return filmStorage.getMostLikedFilms(count);
    }

    public Film getFilmById(Integer id) {
        if (filmStorage.checkFilm(id)) {
            return filmStorage.getFilmById(id);
        } else {
            throw new NotFoundException("Фильма с таким ID нет !");
        }
    }
}
