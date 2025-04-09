package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Пользователь с ID {} успешно добавлен", user.getId());
            return user;
        }


    @PutMapping
    public User updateUser(@Validated(User.ForUpdate.class) @RequestBody User newUser) {
            if (users.containsKey(newUser.getId())) {
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
