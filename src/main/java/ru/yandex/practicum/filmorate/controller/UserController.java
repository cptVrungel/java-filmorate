package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Электронная почта не может быть пустой !");
            } else if (!user.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта должна содержать символ @");
            } else if (user.getLogin() == null || user.getLogin().contains(" ")) {
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения некорректная !");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Пользователь с ID {} успешно добавлен", user.getId());
            return user;
        } catch (ValidationException exc) {
            log.error("Ошибка при добавлении пользователя: {}", exc.getMessage());
            throw exc;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        try {
            if (newUser.getId() == null) {
                throw new ValidationException("Не указан ID пользователя !");
            }
            if (users.containsKey(newUser.getId())) {
                if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                    throw new ValidationException("Электронная почта не может быть пустой !");
                } else if (!newUser.getEmail().contains("@")) {
                    throw new ValidationException("Электронная почта должна содержать символ @ !");
                } else if (newUser.getLogin() == null || newUser.getLogin().contains(" ")) {
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                } else if (newUser.getBirthday().isAfter(LocalDate.now())) {
                    throw new ValidationException("Дата рождения некорректная !");
                }
                User oldUser = users.get(newUser.getId());
                if (newUser.getName() == null || newUser.getName().isBlank()) {
                    oldUser.setName(newUser.getLogin());
                } else {
                    oldUser.setName(newUser.getName());
                }
                oldUser.setEmail(newUser.getEmail());
                oldUser.setLogin(newUser.getLogin());
                oldUser.setBirthday(newUser.getBirthday());
                log.info("Информация о пользователе с ID {} успешно обновлена", oldUser.getId());
                return oldUser;
            } else {
                throw new NotFoundException("Пользователя с таким ID нет !");
            }
        } catch (ValidationException exc) {
            log.error("Ошибка при обновлении пользователя: {}", exc.getMessage());
            throw exc;
        }
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    private Integer getNextId() {
        Integer counter = users.keySet().stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);
        return ++counter;
    }
}
