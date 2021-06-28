package com.utn.udee.service;

import com.utn.udee.exception.AddressExistsException;
import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.model.Address;
import com.utn.udee.model.Client;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.anAddress;
import static com.utn.udee.utils.TestUtils.anAddressesList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AddressServiceTest {

    private AddressRepository addressRepository;
    private AddressService addressService;

    @BeforeEach
    public void setUp(){
        addressRepository = mock(AddressRepository.class);
        addressService = new AddressService(addressRepository);
    }

    @Test
    public void testGetAllAddresses(){
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(3L);
        when(mockedPage.getTotalPages()).thenReturn(4);
        when(mockedPage.getContent()).thenReturn(anAddressesList);
        when(addressRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Address> adressPage = addressService.getAll(pageable);

        assertEquals(3L, adressPage.getTotalElements());
        assertEquals(4, adressPage.getTotalPages());
        assertEquals(anAddressesList, adressPage.getContent());
    }

    @Test
    public void testAddAddressHappyPath(){
        Address toAdd = Address.builder().address("Street 1111")
                .tariff(new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()))
                .build();
        Address newAddress = new Address();
        when(addressRepository.existsByAddress(toAdd.getAddress())).thenReturn(false);
        when(addressRepository.save(any(Address.class))).thenReturn(anAddress);
        try {
            newAddress = addressService.add(toAdd);
        } catch (AddressExistsException e) {
            e.printStackTrace();
        }
        assertNotNull(newAddress.getId());
        assertEquals(toAdd.getAddress(), newAddress.getAddress());
        assertEquals(toAdd.getTariff(), newAddress.getTariff());
    }

    @Test
    public void testAddAddressThowsException(){
        when(addressRepository.existsByAddress(any(String.class))).thenReturn(true);
        assertThrows(AddressExistsException.class, ()->
                addressService.add(anAddress));
    }

    @Test
    public void testGetAddressByIdHappyPath(){
        Address address = new Address();
        when(addressRepository.findById(1)).thenReturn(Optional.ofNullable(anAddress));
        try {
            address = addressService.getById(1);
        } catch (AddressNotExistsException e) {
            assertEquals(1, address.getId());
        }
    }

    @Test
    public void testGetAddressByIdThrowsException(){
        when(addressRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(AddressNotExistsException.class, ()->
                addressService.getById(4));
    }

    @Test
    public void testDeleteAddressByIdHappyPath(){
        when(addressRepository.existsById(1)).thenReturn(true);
        try {
            addressService.deleteById(1);
        } catch (AddressNotExistsException e) {
            e.printStackTrace();
        }
        verify(addressRepository,times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAddressByIdThrowsException(){
        when(addressRepository.existsById(4)).thenReturn(false);

        assertThrows(AddressNotExistsException.class, ()->
                addressService.deleteById(4));
    }

    @Test
    public void testUpdateAdressHappyPath(){
        AddressService mockedService = mock(AddressService.class);
        Address newAddress = Address.builder()
                .address("Street 4444")
                .tariff(new Tariff(4, 4444.21f, TariffType.COMMERCIAL, Collections.emptyList())).build();
        try {
            when(addressRepository.findById(1)).thenReturn(Optional.ofNullable(anAddress));
            when(mockedService.getById(1)).thenReturn(anAddress);
            addressService.update(1, newAddress);
        } catch (AddressNotExistsException e) {
            e.printStackTrace();
        }
        verify(addressRepository,times(1)).save(anAddress);
    }

    @Test
    public void testUpdateAdressThrowsException(){
        AddressService mockedService = mock(AddressService.class);
        try {
            when(mockedService.getById(4)).thenReturn(null);
        } catch (AddressNotExistsException e) {
            e.printStackTrace();
        }
        assertThrows(AddressNotExistsException.class, ()->
                addressService.update(4, anAddress));
    }
}
