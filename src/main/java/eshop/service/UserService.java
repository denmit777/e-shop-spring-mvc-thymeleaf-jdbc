package eshop.service;

import eshop.dto.UserDto;
import eshop.model.User;

public interface UserService {

    User save(UserDto userDto);

    User getByLoginAndPassword(String login, String password);

    boolean isInvalidUser(UserDto userDto);

    String invalidUser(UserDto userDto);
}
