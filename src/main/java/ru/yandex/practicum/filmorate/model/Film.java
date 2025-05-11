package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
public class Film {

    private static final LocalDate DATE = LocalDate.of(1895, 12,28);
    private static final int LENGTHINESS = 200;

    @NotNull(message = "Не указан ID фильма !")
    private Integer id;

    @NotBlank(message = "Название не может быть пустым !")
    private String name;

    private List<Genre> genres;  // теперь список объектов Genre
    private Mpa mpa;

    @NotBlank(message = "Не указано описание фильма !")
    @Size(max = LENGTHINESS, message = "Максимальная длина описания - 200 символов !")
    private String description;

    @NotNull(message = "Не задана дата релиза фильма !")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше нуля !")
    @NotNull(message = "Не задана продолжительность фильма !")
    private int duration;

    @JsonIgnore
    @AssertTrue(message = "Дата релиза - не раньше 28 декабря 1895 !")
    public boolean isReleaseDateValid() {
        return releaseDate != null && !releaseDate.isBefore(DATE);
    }

    public Film() {
    }

    public interface ForUpdate {}

    public interface ForCreate {}
}
