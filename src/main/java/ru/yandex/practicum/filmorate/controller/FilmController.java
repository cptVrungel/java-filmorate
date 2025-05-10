package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.model.DTO.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films/{filmId}")
    public FilmDTO getFilmById(@PathVariable("filmId") @Min(1) Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/films")
    public Collection<FilmDTO> getAll() {
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public FilmDTO addFilm(@Valid @RequestBody NewFilmRequest newFilmRequest) {
        return filmService.addFilm(newFilmRequest);
    }

    @PutMapping("/films")
    public FilmDTO updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Collection<Integer> addLike(@PathVariable("filmId") @Min(1) Integer filmId,
                                @PathVariable("userId") @Min(1) Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Collection<Integer> deleteLike(@PathVariable("filmId") @Min(1) Integer filmId,
                                   @PathVariable("userId") @Min(1) Integer userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getMostLikedFilms(@RequestParam(defaultValue = "10") @Min(1) Integer count) {
        return filmService.getMostLikedFilms(count);
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable("genreId") Integer genreId) {
        return filmService.getGenreById(genreId);
    }

    @GetMapping("/genres")
    public Collection<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable("mpaId") Integer mpaId) {
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getAllMpas() {
        return filmService.getAllMpas();
    }
}
