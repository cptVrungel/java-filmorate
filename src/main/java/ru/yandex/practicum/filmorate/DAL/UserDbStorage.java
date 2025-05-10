package ru.yandex.practicum.filmorate.DAL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("database")
public class UserDbStorage implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(login, email, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, " +
            "birthday = ? WHERE user_id = ?";
    private static final String GET_USER_FRIEND =
            "SELECT u.* " +
                    "FROM users AS u " +
                    "JOIN friends AS f ON u.user_id = f.friend_id " +
                    "WHERE f.user_id = ?";
    private static final String ADD_FRIEND = "MERGE INTO friends (user_id, friend_id) KEY(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    protected final JdbcTemplate jdbc;
    protected final RowMapper<User> mapper;

    public UserDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new UserRowMapper();
    }

    @Override
    public boolean checkUser(Integer id) {
        try {
            User user = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            return true;
        } catch (EmptyResultDataAccessException ignored) {
            return false;
        }
    }

    @Override
    public User getUserById(Integer id) {
        return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public void addUser(User newUser) {
        save(newUser);
    }

    @Override
    public void updateUser(Integer id, User newUser) {
        int rowsUpdated = jdbc.update(
                UPDATE_QUERY,
                newUser.getLogin(),
                newUser.getEmail(),
                newUser.getName(),
                newUser.getBirthday(),
                id);
        newUser.setId(id);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public List<User> getUserFriendList(Integer userId) {
        return jdbc.query(GET_USER_FRIEND, mapper, userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        jdbc.update(ADD_FRIEND, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbc.update(DELETE_FRIEND, userId, friendId);
    }

    @Override
    public Set<User> getCommonFriendList(Integer userId1, Integer userId2) {
        Set<User> friends1 = new HashSet<>(getUserFriendList(userId1));
        Set<User> friends2 = new HashSet<>(getUserFriendList(userId2));

        friends1.retainAll(friends2);
        return friends1;
    }

    private void save(User newUser) {
        int id = insert(
                INSERT_QUERY,
                newUser.getLogin(),
                newUser.getEmail(),
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
    }

    private int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Component
    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setId(resultSet.getInt("user_id"));
            user.setEmail(resultSet.getString("email"));
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("name"));
            user.setBirthday(resultSet.getDate("birthday").toLocalDate());

            return user;
        }
    }
}
