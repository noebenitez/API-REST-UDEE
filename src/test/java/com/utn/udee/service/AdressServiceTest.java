package com.utn.udee.service;

import com.utn.udee.exception.AdressExistsException;
import com.utn.udee.exception.AdressNotExistsException;
import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.model.Client;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.model.dto.AdressDto;
import com.utn.udee.repository.AdressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AdressServiceTest {

    private AdressRepository adressRepository;
    private AdressService adressService;

    private static Adress adressExample = new Adress(1, "Street 1111",
            new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()),
            new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList()));

    private static List<Adress> ADRESSES_LIST = List.of(
            new Adress(1, "Street 1111",
                    new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()),
                    new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList())),
            new Adress(2, "Avenue 2222",
                    new Tariff(2, 222.21f, TariffType.RESIDENTIAL, Collections.emptyList()),
                    new Client(2, "33222111", "Joe", "Doe", "joe1", "2211", Collections.emptyList())));


    @BeforeEach
    public void setUp(){
        adressRepository = mock(AdressRepository.class);
        adressService = new AdressService(adressRepository);
    }

    @Test
    public void testGetAllAdresses(){
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(3L);
        when(mockedPage.getTotalPages()).thenReturn(4);
        when(mockedPage.getContent()).thenReturn(ADRESSES_LIST);
        when(adressRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Adress> adressPage = adressService.getAll(pageable);

        assertEquals(3L, adressPage.getTotalElements());
        assertEquals(4, adressPage.getTotalPages());
        assertEquals(ADRESSES_LIST, adressPage.getContent());
    }

    @Test
    public void testAddAdressHappyPath(){
        Adress toAdd = Adress.builder().adress("Street 1111")
                .tariff(new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()))
                .build();
        Adress newAdress = new Adress();
        when(adressRepository.existsByAdress(toAdd.getAdress())).thenReturn(false);
        when(adressRepository.save(any(Adress.class))).thenReturn(adressExample);
        try {
            newAdress = adressService.add(toAdd);
        } catch (AdressExistsException e) {
            e.printStackTrace();
        }
        assertNotNull(newAdress.getId());
        assertEquals(toAdd.getAdress(), newAdress.getAdress());
        assertEquals(toAdd.getTariff(), newAdress.getTariff());
    }

    @Test
    public void testAddThowsException(){
        when(adressRepository.existsByAdress(any(String.class))).thenReturn(true);
        assertThrows(AdressExistsException.class, ()->
                adressService.add(adressExample));
    }

    @Test
    public void testGetAdressByIdHappyPath(){
        Adress adress = new Adress();
        when(adressRepository.findById(1)).thenReturn(Optional.ofNullable(adressExample));
        try {
            adress = adressService.getById(1);
        } catch (AdressNotExistsException e) {
            assertEquals(1, adress.getId());
        }
    }

    @Test
    public void testGetAdressByIdThrowsException(){
        when(adressRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(AdressNotExistsException.class, ()->
                adressService.getById(4));
    }

    @Test
    public void testDeleteAdressByIdHappyPath(){
        when(adressRepository.existsById(1)).thenReturn(true);
        try {
            adressService.deleteById(1);
        } catch (AdressNotExistsException e) {
            e.printStackTrace();
        }
        verify(adressRepository,times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAdressByIdThrowsException(){
        when(adressRepository.existsById(4)).thenReturn(false);

        assertThrows(AdressNotExistsException.class, ()->
                adressService.deleteById(4));
    }

    @Test
    public void testUpdateAdressHappyPath(){
        AdressService mockedService = mock(AdressService.class);
        Adress newAdress = Adress.builder()
                .adress("Street 4444")
                .tariff(new Tariff(4, 4444.21f, TariffType.COMMERCIAL, Collections.emptyList())).build();
        try {
            when(adressRepository.findById(1)).thenReturn(Optional.ofNullable(adressExample));
            when(mockedService.getById(1)).thenReturn(adressExample);
            adressService.update(1, newAdress);
        } catch (AdressNotExistsException e) {
            e.printStackTrace();
        }
        verify(adressRepository,times(1)).save(adressExample);
    }

    @Test
    public void testUpdateAdressThrowsException(){
        AdressService mockedService = mock(AdressService.class);
        try {
            when(mockedService.getById(4)).thenReturn(null);
        } catch (AdressNotExistsException e) {
            e.printStackTrace();
        }
        assertThrows(AdressNotExistsException.class, ()->
                adressService.update(4, adressExample));
    }
}
