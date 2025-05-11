package ru.yandex.practicum.filmorate.model.DTO;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;

@UtilityClass
public class FilmMapperDTO {

    public Film requestToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setGenres(newFilmRequest.getGenres());
        film.setMpa(newFilmRequest.getMpa());
        film.setDescription(newFilmRequest.getDescription());
        film.setDuration(newFilmRequest.getDuration());
        film.setReleaseDate(newFilmRequest.getReleaseDate());

        return film;
    }

    public FilmDTO filmToFilmDTO(Film film) {
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
