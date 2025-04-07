package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        try {
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Название не может быть пустым !");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания - 200 символов !");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза - не раньше 28 декабря 1985 !");
            } else if (film.getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть больше нуля !");
            }
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен с ID {}", film.getId());
            return film;
        } catch (ValidationException exc) {
            log.error("Ошибка при добавлении фильма: {}", exc.getMessage());
            throw exc;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        try {
            if (newFilm.getId() == null) {
                throw new ValidationException("Не указан ID фильма !");
            }
            if (films.containsKey(newFilm.getId())) {
                if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                    throw new ValidationException("Название не может быть пустым !");
                } else if (newFilm.getDescription().length() > 200) {
                    throw new ValidationException("Максимальная длина описания - 200 символов !");
                } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
                    throw new ValidationException("Дата релиза - не раньше 28 декабря 1985 !");
                } else if (newFilm.getDuration() < 0) {
                    throw new ValidationException("Продолжительность фильма должна быть больше нуля !");
                }
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
        } catch (ValidationException exc) {
            log.error("Ошибка при обновлении фильма: {}", exc.getMessage());
            throw exc;
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
