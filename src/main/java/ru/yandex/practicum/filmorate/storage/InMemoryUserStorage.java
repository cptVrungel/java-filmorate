package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<User>> friends = new HashMap<>();

    @Override
    public void addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
    }

    @Override
    public boolean checkUser(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (!friends.containsKey(userId)) {
            friends.put(userId, new HashSet<>());
        }
        friends.get(userId).add(users.get(friendId));

        if (!friends.containsKey(friendId)) {
            friends.put(friendId, new HashSet<>());
        }
        friends.get(friendId).add(users.get(userId));
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (!friends.containsKey(userId)) {
            return;
        }
        friends.get(userId).remove(users.get(friendId));

        if (!friends.containsKey(friendId)) {
            return;
        }
        friends.get(friendId).remove(users.get(userId));
    }


    @Override
    public Set<User> getUserFriendList(Integer userId) {
        return friends.getOrDefault(userId, Collections.emptySet());
    }

    @Override
    public Set<User> getCommonFriendList(Integer userId1, Integer userId2) {
        Set<User> friendsUser1 = getUserFriendList(userId1);
        Set<User> commonFriends = getUserFriendList(userId2).stream()
                .filter(user -> friendsUser1.contains(user))
                .collect(Collectors.toSet());
        return commonFriends;
    }

    private Integer getNextId() {
        Integer counter = users.keySet().stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);
        return ++counter;
    }
}
