package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    void addUser(User user);

    //public void updateUser(User newUser);
    boolean checkUser(Integer id);

    User getUserById(Integer id);

    Collection<User> getAllUsers();

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    Set<User> getUserFriendList(Integer userId);

    Set<User> getCommonFriendList(Integer userId1, Integer userId2);

}
