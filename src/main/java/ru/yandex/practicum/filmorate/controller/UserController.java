package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

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

    @PostMapping
    public User addUser(@Validated(User.ForCreate.class) @RequestBody User user) {
        return userService.addUser(user);
    }


    @PutMapping
    public User updateUser(@Validated(User.ForUpdate.class) @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") @Min(1) Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public Set<User> addFriend(@PathVariable("userId") @Min(1) Integer userId,
                               @PathVariable("friendId") @Min(1) Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<User> getUserFriendList(@PathVariable("userId") @Min(1) Integer userId) {
        return userService.getUserFriendList(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Set<User> deleteFriend(@PathVariable("userId") @Min(1) Integer userId,
                                  @PathVariable("friendId") @Min(1) Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonUserFriendList(@PathVariable("id") @Min(1) Integer id,
                                             @PathVariable("otherId") @Min(1) Integer otherId) {
        return userService.getCommonFriendList(id, otherId);
    }
}
