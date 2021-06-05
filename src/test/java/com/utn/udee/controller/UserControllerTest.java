package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.controller.converter.UserToUserDTOConverter;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.*;
import com.utn.udee.service.TariffService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private ObjectMapper objectMapper;
    private UserToUserDTOConverter userDTOConverter;
    private UserController userController;
    private EntityURLBuilder entityURLBuilder;

    private static User userExample = new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList());

    private static UserDto userDtoExample = new UserDto(1, "11222333", "John", "Doe", "john1");

    private static List<User> USERS_LIST = List.of(
            new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList()),
            new Employee(2, "99888777", "Rachel", "Smiths", "smiths1", "2211"));

    /*Result of User list mapped to UserDto*/
    private static List<UserDto> USERSDTO_LIST = List.of(
            new UserDto(1, "11222333", "John", "Doe", "john1"),
            new UserDto(2, "99888777", "Rachel", "Smiths", "smiths1"));




    @BeforeEach
    public void setUp(){
        userService = mock(UserService.class);
        objectMapper = mock(ObjectMapper.class);
        userDTOConverter = Mockito.mock(UserToUserDTOConverter.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        userController = new UserController(userService, objectMapper, userDTOConverter);
    }

    @Test
    public void testGetAllUsersNoContent(){
        //Given

        Pageable pageable = PageRequest.of(50, 10);
        Page<UserDtoI> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(userService.getAll(pageable)).thenReturn(mockedPage);

        //Then
        ResponseEntity<List<UserDtoI>> response = userController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllUsersOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(2L);
        when(mockedPage.getTotalPages()).thenReturn(1);

        when(mockedPage.getContent()).thenReturn(USERSDTO_LIST);
        when(userService.getAll(pageable)).thenReturn(mockedPage);

        //Then
        ResponseEntity<List<UserDtoI>> response = userController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(USERSDTO_LIST, response.getBody());
    }


    /*Test for scenarios of method  public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
     */
/*    @Test
    public void testLoginOk(){
        //Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("john1", "1122");
        LoginResponseDto loginResponseDto = mock(LoginResponseDto.class);
        when(loginResponseDto.getToken()).thenReturn("token12345");

        final UserController mockUserController = mock(UserController.class);
        when(userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword())).thenReturn(userExample);
        ResponseEntity res = mock(ResponseEntity.class);
        when(res.getStatusCode()).thenReturn(HttpStatus.OK);
        when(res.getBody()).thenReturn("token12345");



        ResponseEntity<LoginResponseDto> response = userController.login(loginRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponseDto.getToken(), response.getBody());
    }*/

    @Test
    public void testLoginUnauthorized(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("notAUser", "3322");
        when(userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword())).thenReturn(null);

        ResponseEntity<LoginResponseDto> response = userController.login(loginRequestDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
