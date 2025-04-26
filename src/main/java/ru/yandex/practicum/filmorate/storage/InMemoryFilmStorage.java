package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
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
}

