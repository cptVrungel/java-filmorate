package ru.yandex.practicum.filmorate.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
