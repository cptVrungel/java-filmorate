package ru.yandex.practicum.filmorate.model.DTO;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapperDTO {
    public static Film requestToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setGenres(newFilmRequest.getGenres());
        film.setMpa(newFilmRequest.getMpa());
        film.setDescription(newFilmRequest.getDescription());
        film.setDuration(newFilmRequest.getDuration());
        film.setReleaseDate(newFilmRequest.getReleaseDate());

        return film;
    }

    public static FilmDTO filmToFilmDTO(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setGenres(film.getGenres());
        filmDTO.setMpa(film.getMpa());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        return filmDTO;
    }
}
