package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DTO.NewUserRequest;
import ru.yandex.practicum.filmorate.model.DTO.UserDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable("userId") @Min(1) Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @PutMapping
    public UserDTO updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user.getId(), user);
    }

    @GetMapping("/{userId}/friends")
    public Collection<UserDTO> getUserFriendList(@PathVariable("userId") @Min(1) Integer userId) {
        return userService.getUserFriendList(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public Collection<UserDTO> addFriend(@PathVariable("userId") @Min(1) Integer userId,
                               @PathVariable("friendId") @Min(1) Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Collection<UserDTO> deleteFriend(@PathVariable("userId") @Min(1) Integer userId,
                                  @PathVariable("friendId") @Min(1) Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDTO> getCommonUserFriendList(@PathVariable("id") @Min(1) Integer id,
                                             @PathVariable("otherId") @Min(1) Integer otherId) {
        return userService.getCommonFriendList(id, otherId);
    }
}
