package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        //user.setId(getNextId());
        userStorage.addUser(user);
        log.info("Пользователь с ID {} успешно добавлен", user.getId());
        return user;
    }

    public User updateUser(User newUser) {
        if (userStorage.checkUser(newUser.getId())) {
            User oldUser = userStorage.getUserById(newUser.getId());
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

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        if (userStorage.checkUser(id)) {
            return userStorage.getUserById(id);
        } else {
            throw new NotFoundException("Фильма с таким ID нет !");
        }
    }

    public Set<User> addFriend(Integer userId, Integer friendId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя c ID " + userId + " нет !");
        }
        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователя c ID " + friendId + " нет !");
        }
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи с ID {} и {} теперь друзья !", userId, friendId);
        return userStorage.getUserFriendList(userId);
    }

    public Set<User> deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя c ID " + userId + " нет !");
        }
        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователя c ID " + friendId + " нет !");
        }
        if (friendId <= 0 || userId <= 0) {
            throw new ValidationException("ID пользователей не может быть меньше 0 !");
        }
        userStorage.deleteFriend(userId, friendId);
        log.info("Пользователи с ID {} и {} больше не друзья !", userId, friendId);
        return userStorage.getUserFriendList(userId);
    }

    public Set<User> getUserFriendList(Integer userId) {
        if (userId <= 0) {
            throw new ValidationException("ID пользователей не может быть меньше 0 !");
        }
        if (userStorage.checkUser(userId)) {
            return userStorage.getUserFriendList(userId);
        } else {
            throw new NotFoundException("Пользователя с таким ID нет !");
        }
    }

    public Set<User> getCommonFriendList(Integer userId1, Integer userId2) {
        if (userId1 <= 0 || userId2 <= 0) {
            throw new ValidationException("ID пользователей не может быть меньше 0 !");
        }
        if (!userStorage.checkUser(userId1)) {
            throw new NotFoundException("Пользователя c ID " + userId1 + " нет !");
        }
        if (!userStorage.checkUser(userId2)) {
            throw new NotFoundException("Пользователя c ID " + userId2 + " нет !");
        }
        return userStorage.getCommonFriendList(userId1, userId2);
    }
}
