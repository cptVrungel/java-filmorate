package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.model.DTO.FilmMapperDTO;
import ru.yandex.practicum.filmorate.model.DTO.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("database") FilmStorage filmStorage, @Qualifier("database") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmDTO getFilmById(Integer id) {
        if (filmStorage.checkFilm(id)) {
            return FilmMapperDTO.filmToFilmDTO(filmStorage.getFilmById(id));
        } else {
            throw new NotFoundException("Фильма с таким ID нет !");
        }
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(film -> FilmMapperDTO.filmToFilmDTO(film))
                .collect(Collectors.toList());
    }

    public FilmDTO addFilm(NewFilmRequest newFilmRequest) {
        if (newFilmRequest.getMpa() != null &&
                (newFilmRequest.getMpa().getId() > 5 || newFilmRequest.getMpa().getId() < 0)) {
            throw new NotFoundException("Допустимые id mpa - от 1 до 5");
        } else if (newFilmRequest.getGenres() != null && !newFilmRequest.getGenres().isEmpty() && newFilmRequest.getGenres().stream()
                .anyMatch(genre -> genre.getId() < 0 || genre.getId() > 7)) {
            throw new NotFoundException("Допустимые id жанра - от 1 до 6");
        }
        Film newFilm = FilmMapperDTO.requestToFilm(newFilmRequest);
        filmStorage.addFilm(newFilm);
        log.info("Фильм успешно добавлен с ID {}", newFilm.getId());
        return FilmMapperDTO.filmToFilmDTO(filmStorage.getFilmById(newFilm.getId()));
    }

    public FilmDTO updateFilm(Film film) {
        if (filmStorage.checkFilm(film.getId())) {
            /*if(film.getMpa() != null &&
                    (film.getMpa().getId() > 5 || film.getMpa().getId() < 0)){
                throw new NotFoundException("Допустимые id mpa - от 1 до 5");
            } else if (film.getGenres() != null && film.getGenres().stream()
                    .allMatch(genre -> genre.getId() > 0 && genre.getId() < 7)) {
                throw new NotFoundException("Допустимые id жанра - от 1 до 6");
            }*/
            filmStorage.updateFilm(film);
            log.info("Информация о фильме с ID {} успешно обновлена", film.getId());
            return FilmMapperDTO.filmToFilmDTO(film);
        } else {
            throw new NotFoundException("Фильма с таким ID нет !");
        }
    }

    public Collection<Integer> addLike(Integer filmId, Integer userId) {
        if (!filmStorage.checkFilm(filmId)) {
            throw new NotFoundException("Фильма с ID " + filmId + " нет !");
        }
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с ID " + userId + " нет !");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с ID {} успешно лайкнул фильм с ID {}", userId, filmId);
        return filmStorage.getFilmLikes(filmId);
    }

    public Collection<Integer> deleteLike(Integer filmId, Integer userId) {
        if (!filmStorage.checkFilm(filmId)) {
            throw new NotFoundException("Фильма с ID " + filmId + " нет !");
        }
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с ID " + userId + " нет !");
        }
        filmStorage.deleteLike(filmId, userId);
        log.info("Удален лайк пользователя с ID {} фильму с ID {}", userId, filmId);
        return filmStorage.getFilmLikes(filmId);
    }

    public Collection<Film> getMostLikedFilms(Integer count) {
        return filmStorage.getMostLikedFilms(count);
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId < 0 || genreId > 6) {
            throw new NotFoundException("Допустимые id жанра - от 1 до 6");
        }
        return filmStorage.getGenreById(genreId);
    }

    public Collection<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Mpa getMpaById(Integer mpaId) {
        if (mpaId < 0 || mpaId > 5) {
            throw new NotFoundException("Допустимые id mpa - от 1 до 5");
        }
        return filmStorage.getMpaById(mpaId);
    }

    public Collection<Mpa> getAllMpas() {
        return filmStorage.getAllMpas();
    }
}
