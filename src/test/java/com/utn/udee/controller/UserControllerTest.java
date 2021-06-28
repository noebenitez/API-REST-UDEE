package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.controller.converter.UserToUserDTOConverter;
import com.utn.udee.exception.FailTokenException;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.*;
import com.utn.udee.service.InvoiceService;
import com.utn.udee.service.TariffService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static com.utn.udee.utils.TestUtils.aUsersDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private ObjectMapper objectMapper;
    private UserToUserDTOConverter userDTOConverter;
    private UserController userController;
    private EntityURLBuilder entityURLBuilder;
    private ModelMapper modelMapper;
    private InvoiceService invoiceService;
    private ConversionService conversionService;

    @BeforeEach
    public void setUp(){
        userService = mock(UserService.class);
        objectMapper = mock(ObjectMapper.class);
        userDTOConverter = Mockito.mock(UserToUserDTOConverter.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        modelMapper = mock(ModelMapper.class);
        invoiceService =  mock(InvoiceService.class);
        conversionService = mock(ConversionService.class);
        userController = new UserController(userService, objectMapper,modelMapper,userDTOConverter,invoiceService, conversionService);
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

        when(mockedPage.getContent()).thenReturn(aUsersDtoList);
        when(userService.getAll(pageable)).thenReturn(mockedPage);

        //Then
        ResponseEntity<List<UserDtoI>> response = userController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(aUsersDtoList, response.getBody());
    }

    @Test
    public void testLoginUnauthorized() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto("notAUser", "3322");
        when(userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword())).thenReturn(null);

        ResponseEntity<LoginResponseDto> response = userController.login(loginRequestDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
