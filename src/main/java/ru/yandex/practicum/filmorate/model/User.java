package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class User {

    private static final char DOG = '@';

    @NotNull(groups = Film.ForUpdate.class, message = "Не указан ID пользователя !")
    private Integer id;

    @NotBlank(message = "Электронная почта не может быть пустой !")
    private String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы !")
    private String login;

    private String name;

    private LocalDate birthday;

    @JsonIgnore
    @AssertTrue(message = "Дата рождения некорректная !")
    public boolean isBirthdayValid() {
        return birthday.isBefore(LocalDate.now());
    }

    @JsonIgnore
    @AssertTrue(message = "Электронная почта должна содержать символ @ !")
    public boolean isEmailValid() {
        return email.contains(String.valueOf(DOG));
    }

    public User() {
    }

    public interface ForUpdate {}
}
