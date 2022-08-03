package eshop.converter;

import eshop.dto.UserDto;
import eshop.model.User;

public interface UserConverter {

    User fromUserDto(UserDto userDto);
}
