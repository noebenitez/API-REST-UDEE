package com.utn.udee.service;

import com.utn.udee.exception.*;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.model.dto.UserDtoI;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private AddressService addressService;
    private UserService userService;
    private MeasurementService measurementService;
    private UserProjection aUserProjection;
    private List<UserProjection> aUserProjectionList;

    @BeforeEach
    public void setUp(){
        userRepository = mock(UserRepository.class);
        addressService = mock(AddressService.class);
        measurementService = mock(MeasurementService.class);
        userService = new UserService(userRepository,measurementService,addressService);

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        aUserProjection = factory.createProjection(UserProjection.class);
        aUserProjection.setUsername("NN");
        aUserProjection.setFirstname("NNN");
        aUserProjection.setLastname("MM");
        aUserProjection.setSum(2.5f);
        aUserProjection.setDni("1234");

        aUserProjectionList = List.of(aUserProjection);

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


    @Test
    public void testGetTopTenConsumersOk()
    {
        when(measurementService.getTop10Consumers(any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aUserProjectionList);

        List<UserProjection> users = userService.getTop10Consumers(LocalDateTime.now(),LocalDateTime.now());

        ///then

        assertEquals(aUserProjection.getDni(),users.get(0).getDni());
        assertEquals(1,users.size());

    }

    @Test
    public void testGetRangeDateConsumptionOk()
    {
        when(measurementService.getRangeDateConsumption(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
        ///then
        List<Measurement> measurements = userService.getRangeDateConsumption(IDUSER,LocalDateTime.now(),LocalDateTime.now());

        assertEquals(aMeasurement.getId(),measurements.get(0).getId());
        assertEquals(1,measurements.size());

    }

    @Test
    public void testGetMeasurementsFromDateRangeOk()
    {
        try {
            when(addressService.getById(anyInt())).thenReturn(anAddress);
            when(measurementService.getMeasurementsByAddressRangeDate(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
            List<Measurement> measurements = userService.getMeasurementsFromDateRange(IDADDRESS,LocalDateTime.now(),LocalDateTime.now());
          ///then
            assertEquals(aMeasurement.getId(),measurements.get(0).getId());
            assertEquals(1,measurements.size());
        } catch (AddressNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }
    }




}
