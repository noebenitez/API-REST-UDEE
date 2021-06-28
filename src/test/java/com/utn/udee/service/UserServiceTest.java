package com.utn.udee.service;

import com.utn.udee.exception.*;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.model.dto.UserDtoI;
import com.utn.udee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.aUserClient;
import static com.utn.udee.utils.TestUtils.aUsersDtoList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private AddressService addressService;
    private UserService userService;
    private MeasurementService measurementService;

    @BeforeEach
    public void setUp(){
        userRepository = mock(UserRepository.class);
        addressService = mock(AddressService.class);
        measurementService = mock(MeasurementService.class);
        userService = new UserService(userRepository,measurementService,addressService);
    }

    @Test
    public void testAddUserHappyPath(){
        User toAdd = new Client();
        toAdd.setDni("11222333");
        toAdd.setFirstname("John");
        toAdd.setLastname("Doe");
        toAdd.setUsername("john1");
        toAdd.setPassword("1122");
        User newClient = new Client();
        when(userRepository.existsByDni(toAdd.getDni())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(aUserClient);
        try {
            newClient = userService.add(toAdd);
        } catch (UserExistsException e) {
            e.printStackTrace();
        }
        assertNotNull(newClient.getId());
        assertEquals(toAdd.getDni(), newClient.getDni());
        assertEquals(toAdd.getFirstname(), newClient.getFirstname());
        assertEquals(toAdd.getLastname(), newClient.getLastname());
        assertEquals(toAdd.getUsername(), newClient.getUsername());
        assertEquals(toAdd.getPassword(), newClient.getPassword());
    }

    @Test
    public void testAddUserThrowsException(){
        when(userRepository.existsByDni((any(String.class)))).thenReturn(true);
        assertThrows(UserExistsException.class, ()->
                userService.add(aUserClient));
    }

    @Test
    public void testLoginHappyPath(){
        when(userRepository.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(aUserClient);
        User user = userService.login("john1", "1122");
        assertEquals("john1", user.getUsername());
        assertEquals("1122", user.getPassword());
    }


    @Test
    public void testGetAllUsers(){
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(2L);
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(mockedPage.getContent()).thenReturn(aUsersDtoList);
        when(userRepository.findAllDto(pageable)).thenReturn(mockedPage);

        Page<UserDtoI> userPage = userService.getAll(pageable);

        assertEquals(2L, userPage.getTotalElements());
        assertEquals(1, userPage.getTotalPages());
        assertEquals(aUsersDtoList, userPage.getContent());
    }

    @Test
    public void testGetUserByIdHappyPath(){
        Client client = new Client();
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(aUserClient));
        try {
            client = (Client) userService.getById(1);
        } catch (UserNotExistsException e) {
            assertEquals(1, client.getId());
        }
    }

    @Test
    public void testGetUserByIdThrowsException(){
        when(userRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(UserNotExistsException.class, ()->
                userService.getById(4));
    }


}
