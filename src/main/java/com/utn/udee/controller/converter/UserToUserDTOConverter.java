package com.utn.udee.controller.converter;

import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.Employee;
import com.utn.udee.model.User;
import com.utn.udee.model.UserType;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.repository.UserRepository;
import com.utn.udee.service.UserService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDto> {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserToUserDTOConverter(final ModelMapper modelMapper, final UserService userService){
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public UserDto convert(User source){
        return modelMapper.map(source, UserDto.class);
    }

    @SneakyThrows
    public User convertReverse(UserDto userDto)  {
        return userService.getById(userDto.getId());
    }

/*    public List<UserDto> convertList(List<User> source){
        return source.stream().map(o -> UserDto.
                builder()
                .id(o.getId())
                .dni(o.getDni())
                .firstname(o.getFirstname())
                .lastname(o.getLastname())
                .username(o.getUsername())
                .build())
                .collect(Collectors.toList());
    }*/

}
