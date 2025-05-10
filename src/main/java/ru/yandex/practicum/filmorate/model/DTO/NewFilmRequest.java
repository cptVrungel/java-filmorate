package ru.yandex.practicum.filmorate.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {

    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private static final int LENGTHINESS = 200;

    @NotBlank(message = "Название не может быть пустым !")
    private String name;

    private List<Genre> genres;  // теперь список объектов Genre
    private Mpa mpa;


    /*@Size(min = 1, message = "Нужно указать хотя бы один жанр")
    @NotNull(message = "Жанры не должны быть null")
    private List<@NotNull(message = "ID жанра не может быть null") Integer> genre;

    @Pattern(
            regexp = "G|PG|PG-13|R|NC-17",
            message = "Недопустимое значение рейтинга MPA! Доступные варианты: G, PG, PG-13, R, NC-17")
    @NotBlank(message = "Название рейтинга не может быть пустым !")
    private String mpa;*/

    @NotBlank(message = "Не указано описание фильма !")
    @Size(max = LENGTHINESS, message = "Максимальная длина описания - 200 символов !")
    private String description;

    @NotNull(message = "Не задана дата релиза фильма !")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше нуля !")
    @NotNull(message = "Не задана продолжительность фильма !")
    private int duration;

    public NewFilmRequest() {
    }

    @JsonIgnore
    @AssertTrue(message = "Дата релиза - не раньше 28 декабря 1895 !")
    public boolean isReleaseDateValid() {
        return releaseDate != null && !releaseDate.isBefore(DATE);
    }
}
