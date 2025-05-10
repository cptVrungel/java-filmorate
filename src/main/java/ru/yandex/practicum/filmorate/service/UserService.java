package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.DTO.NewUserRequest;
import ru.yandex.practicum.filmorate.model.DTO.UserDTO;
import ru.yandex.practicum.filmorate.model.DTO.UserMapperDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("database") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDTO getUserById(Integer id) {
        if (userStorage.checkUser(id)) {
            return UserMapperDTO.userToUserDTO(userStorage.getUserById(id));
        } else {
            throw new NotFoundException("Пользователя с таким ID нет !");
        }
    }

    public Collection<UserDTO> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(user -> UserMapperDTO.userToUserDTO(user))
                .collect(Collectors.toList());
    }

    public UserDTO addUser(NewUserRequest newUserRequest) {
        User newUser = UserMapperDTO.requestToUser(newUserRequest);
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        userStorage.addUser(newUser);
        log.info("Пользователь успешно добавлен с ID {}", newUser.getId());
        return UserMapperDTO.userToUserDTO(newUser);
    }

    public UserDTO updateUser(Integer id, User user) {
        if (userStorage.checkUser(id)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userStorage.updateUser(id, user);
            log.info("Информация о пользователе с ID {} успешно обновлена", user.getId());
            return UserMapperDTO.userToUserDTO(user);
        } else {
            throw new NotFoundException("Пользователя с таким ID нет !");
        }
    }

    public Collection<UserDTO> getUserFriendList(Integer userId) {
        if (userStorage.checkUser(userId)) {
            return userStorage.getUserFriendList(userId).stream()
                    .map(user -> UserMapperDTO.userToUserDTO(user))
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Пользователя с таким ID нет !");
        }
    }

    public Collection<UserDTO> addFriend(Integer userId, Integer friendId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя c ID " + userId + " нет !");
        }
        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователя c ID " + friendId + " нет !");
        }
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи с ID {} и {} теперь друзья !", userId, friendId);
        return userStorage.getUserFriendList(userId).stream()
                .map(user -> UserMapperDTO.userToUserDTO(user))
                .collect(Collectors.toList());
    }

    public Collection<UserDTO> deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя c ID " + userId + " нет !");
        }
        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователя c ID " + friendId + " нет !");
        }
        userStorage.deleteFriend(userId, friendId);
        log.info("Пользователи с ID {} и {} больше не друзья !", userId, friendId);
        return userStorage.getUserFriendList(userId).stream()
                .map(user -> UserMapperDTO.userToUserDTO(user))
                .collect(Collectors.toList());
    }

    public Set<UserDTO> getCommonFriendList(Integer userId1, Integer userId2) {
        if (!userStorage.checkUser(userId1)) {
            throw new NotFoundException("Пользователя c ID " + userId1 + " нет !");
        }
        if (!userStorage.checkUser(userId2)) {
            throw new NotFoundException("Пользователя c ID " + userId2 + " нет !");
        }
        return userStorage.getCommonFriendList(userId1, userId2).stream()
                .map(user -> UserMapperDTO.userToUserDTO(user))
                .collect(Collectors.toSet());
    }
}
