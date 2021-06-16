package com.utn.udee.controller.converter;

import com.utn.udee.model.User;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.service.UserService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDto> {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserToUserDTOConverter(final ModelMapper modelMapper, final UserService userService){
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public UserDto
    convert(User source){
        return modelMapper.map(source, UserDto.class);
    }

/*    public User convertReverse(UserDto userDto)  {
        return userService.getById(userDto.getId());
    }*/

}
