/*package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("memory")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private final Comparator<Integer> comparator = (film1Id, film2Id) -> {
        int count1 = getFilmLikes(film1Id).size();
        int count2 = getFilmLikes(film2Id).size();
        return Integer.compare(count2, count1);
    };

    @Override
    public void addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);

    }

    @Override
    public boolean checkFilm(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }
        likes.get(filmId).add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        if (!likes.containsKey(filmId)) {
            return;
        }
        likes.get(filmId).remove(userId);
    }

    @Override
    public Set<Integer> getFilmLikes(Integer filmId) {
        return likes.getOrDefault(filmId, Collections.emptySet());
    }

    @Override
    public void updateFilm(Film newFilm) {

    }

    @Override
    public Genre getGenreById(Integer genreId) {
        return null;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return List.of();
    }

    @Override
    public Mpa getMpaById(Integer mpaId) {
        return null;
    }

    @Override
    public Collection<Mpa> getAllMpas() {
        return List.of();
    }

    @Override
    public Collection<Genre> getFilmGenres(Integer filmId) {
        return List.of();
    }

    @Override
    public Collection<Film> getMostLikedFilms(Integer count) {
        if (likes.isEmpty()) {
            return Collections.emptyList();
        }
        if (likes.size() < count) {
            count = likes.size();
        }
        return likes.keySet().stream()
                .sorted(comparator)
                .limit(count)
                .map(filmId -> films.get(filmId))
                .collect(Collectors.toList());
    }

    private Integer getNextId() {
        Integer counter = films.keySet().stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);
        return ++counter;
    }
}*/

