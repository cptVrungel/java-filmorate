package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Film {

    private static final LocalDate DATE = LocalDate.of(1895, 12,28);
    private static final int LENGTHINESS = 200;

    @NotNull(groups = ForUpdate.class, message = "Не указан ID фильма !")
    private Integer id;

    @NotBlank(groups = ForCreate.class, message = "Название не может быть пустым !")
    private String name;

    @NotBlank(groups = ForCreate.class, message = "Не указано описание фильма !")
    @Size(groups = {ForUpdate.class, ForCreate.class}, max = LENGTHINESS, message = "Максимальная длина описания - 200 символов !")
    private String description;

    @NotNull(groups = User.ForCreate.class, message = "Не задана дата релиза фильма !")
    private LocalDate releaseDate;

    @Positive(groups = {ForUpdate.class, ForCreate.class}, message = "Продолжительность фильма должна быть больше нуля !")
    @NotNull(groups = User.ForCreate.class, message = "Не задана продолжительность фильма !")
    private int duration;

    @JsonIgnore
    @AssertTrue(groups = {ForUpdate.class, ForCreate.class}, message = "Дата релиза - не раньше 28 декабря 1895 !")
    public boolean isReleaseDateValid() {
        return releaseDate != null && !releaseDate.isBefore(DATE);
    }

    public Film() {
    }

    public interface ForUpdate {}

    public interface ForCreate {}
}
