package com.utn.udee.controller;

import com.utn.udee.exception.AddressExistsException;
import com.utn.udee.exception.GenericWebException;
import com.utn.udee.exception.IncorrectUserTypeException;
import com.utn.udee.model.Address;
import com.utn.udee.model.Client;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.model.dto.AddressDto;
import com.utn.udee.model.dto.TariffDto;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.service.AddressService;
import com.utn.udee.service.TariffService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressControllerTest {

    private AddressService addressService;
    private TariffService tariffService;
    private UserService userService;
    private ConversionService conversionService;
    private AddressController addressController;
    private EntityURLBuilder entityURLBuilder;



    @BeforeEach
    public void setUp(){
        addressService = mock(AddressService.class);
        tariffService = mock(TariffService.class);
        userService = mock(UserService.class);
        conversionService = Mockito.mock(ConversionService.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        addressController = new AddressController(addressService, tariffService, userService, conversionService);
    }

    @Test
    public void testAddAddressCreated() throws Exception{
        ResponseEntity response = null;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.getById(any(Integer.class))).thenReturn(aUserClient);
        when(addressService.add(any(Address.class))).thenReturn(anAddress);

        response = addressController.addAddress(anAddressDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost/addresses/1", response.getHeaders().getLocation().toString());
    }

    @Test
    public void testAddAddressThrowsIncorrectUserTypeException() throws Exception{
        ResponseEntity response = null;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.getById(any(Integer.class))).thenReturn(aUserEmployee);
        when(addressService.add(any(Address.class))).thenReturn(anAddress);

        assertThrows(IncorrectUserTypeException.class, ()->
                addressController.addAddress(anAddressDto));
    }

    @Test
    public void testAddAddressThrowsAddressExistsException() throws Exception{

        when(userService.getById(any(Integer.class))).thenReturn(aUserClient);
        when(addressService.add(any(Address.class))).thenThrow(AddressExistsException.class);

        assertThrows(AddressExistsException.class, ()->
                addressController.addAddress(anAddressDto));
    }

    @Test
    public void testGetAllAddressesNoContent(){
        //Given

        Pageable pageable = PageRequest.of(50, 10);
        Page<Address> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(addressService.getAll(pageable)).thenReturn(mockedPage);
        when(mockedPage.map(Mockito.any())).thenReturn(mock(Page.class));

        //Then
        ResponseEntity<List<AddressDto>> response = addressController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllAddressesOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(2L);
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(mockedPage.getContent()).thenReturn(anAddressesList);
        when(addressService.getAll(pageable)).thenReturn(mockedPage);

        Page mockedPageDto = mock(Page.class);
        when(mockedPageDto.getTotalElements()).thenReturn(2L);
        when(mockedPageDto.getTotalPages()).thenReturn(1);
        when(mockedPageDto.getContent()).thenReturn(anAddressesDtoList);

        when(mockedPage.map(any())).thenReturn(mockedPageDto);

        //Then
        ResponseEntity<List<AddressDto>> response = addressController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(anAddressesDtoList, response.getBody());
    }

    @Test
    public void testDeleteAdressOk() throws Exception{
        Mockito.doNothing().when(addressService).deleteById(any(Integer.class));
        ResponseEntity response = addressController.deleteAddress(any(Integer.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateAdressOk() throws Exception{
        Mockito.doNothing().when(addressService).update(any(Integer.class), any(Address.class));
        ResponseEntity response = addressController.updateAddress(any(Integer.class), any(Address.class));
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
}
