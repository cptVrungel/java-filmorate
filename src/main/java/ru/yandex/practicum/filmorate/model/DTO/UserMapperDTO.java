package ru.yandex.practicum.filmorate.model.DTO;

import ru.yandex.practicum.filmorate.model.User;

public class UserMapperDTO {
    public static User requestToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setLogin(newUserRequest.getLogin());
        user.setName(newUserRequest.getName());
        user.setBirthday(newUserRequest.getBirthday());

        return user;
    }

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setName(user.getName());
        userDTO.setBirthday(user.getBirthday());

        return userDTO;
    }
}
