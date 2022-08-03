package eshop.service.impl;

import eshop.converter.UserConverter;
import eshop.dao.UserDAO;
import eshop.dto.UserDto;
import eshop.model.User;
import eshop.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class.getName());

    private static final String FIELD_IS_EMPTY = "Login or password shouldn't be empty";
    private static final String INVALID_FIELD = "Login or password shouldn't be less than 4 symbols";
    private static final String USER_IS_PRESENT = "User with login {} is already present";

    private final UserDAO userDAO;
    private final UserConverter userConverter;

    public UserServiceImpl(UserDAO userDAO, UserConverter userConverter) {
        this.userDAO = userDAO;
        this.userConverter = userConverter;
    }

    @Override
    public User save(UserDto userDto) {
        User user = userConverter.fromUserDto(userDto);

        userDAO.save(user);

        LOGGER.info("New user : {}", user.getLogin());

        return user;
    }

    @Override
    public User getByLoginAndPassword(String login, String password) {
        return userDAO.getByLoginAndPassword(login, password);
    }

    @Override
    public boolean isInvalidUser(UserDto userDto) {
        return userDto.getLogin().length() < 4 || userDto.getPassword().length() < 4 || isUserPresent(userDto);
    }

    @Override
    public String invalidUser(UserDto userDto) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();

        if (login.isEmpty() || password.isEmpty()) {
            LOGGER.error(FIELD_IS_EMPTY);

            return FIELD_IS_EMPTY;
        }

        if (isUserPresent(userDto)) {
            LOGGER.error(USER_IS_PRESENT, login);

            return String.format(USER_IS_PRESENT.replace("{}", "%s"), login);
        }

        LOGGER.error(INVALID_FIELD);

        return INVALID_FIELD;
    }

    private boolean isUserPresent(UserDto userDto) {
        String login = userDto.getLogin();

        return getAll().stream().anyMatch(user -> login.equals(user.getLogin()));
    }

    private List<User> getAll() {
        return userDAO.getAll();
    }
}
