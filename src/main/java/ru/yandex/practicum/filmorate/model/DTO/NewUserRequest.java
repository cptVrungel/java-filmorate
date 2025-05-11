package ru.yandex.practicum.filmorate.model.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {

    @Email(message = "Неверный формат электронной почты !")
    @NotBlank(message = "Не указана почта пользователя !")
    private String email;

    @NotBlank(message = "Логин не может быть пустым !")
    @Pattern(regexp = "\\S+",
            message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения некорректная !")
    @NotNull(message = "Не задана дата рождения !")
    private LocalDate birthday;

    public NewUserRequest() {
    }

}
