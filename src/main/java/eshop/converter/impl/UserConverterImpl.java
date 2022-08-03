package eshop.converter.impl;

import eshop.converter.UserConverter;
import eshop.dto.UserDto;
import eshop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public User fromUserDto(UserDto userDto) {
        User user = new User();

        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
