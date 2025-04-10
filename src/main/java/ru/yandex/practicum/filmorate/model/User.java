package ru.yandex.practicum.filmorate.model;

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
public class User {

    @NotNull(groups = User.ForUpdate.class, message = "Не указан ID пользователя !")
    private Integer id;

    @Email(groups = {User.ForUpdate.class, User.ForCreate.class}, message = "Неверный формат электронной почты !")
    @NotBlank(groups = User.ForCreate.class, message = "Не указана почта пользователя !")
    private String email;

    @NotBlank(groups = User.ForCreate.class, message = "Логин не может быть пустым !")
    @Pattern(groups = {User.ForUpdate.class, User.ForCreate.class},
            regexp = "\\S+",
            message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(groups = {User.ForUpdate.class, User.ForCreate.class},
            message = "Дата рождения некорректная !")
    @NotNull(groups = User.ForCreate.class, message = "Не задана дата рождения !")
    private LocalDate birthday;

    public User() {
    }

    public interface ForUpdate {}

    public interface ForCreate {
    }
}
