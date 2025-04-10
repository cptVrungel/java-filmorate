package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Validated(Film.ForCreate.class) @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен с ID {}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Validated(Film.ForUpdate.class) @RequestBody Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldfilm = films.get(newFilm.getId());
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

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private Integer getNextId() {
        Integer counter = films.keySet().stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);
        return ++counter;
    }
}
