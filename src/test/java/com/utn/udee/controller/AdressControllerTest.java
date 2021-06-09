package com.utn.udee.controller;

import com.utn.udee.exception.AdressExistsException;
import com.utn.udee.exception.GenericWebException;
import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.model.Client;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.model.dto.AdressDto;
import com.utn.udee.model.dto.TariffDto;
import com.utn.udee.service.AdressService;
import com.utn.udee.service.TariffService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdressControllerTest {

    private AdressService adressService;
    private TariffService tariffService;
    private ConversionService conversionService;
    private AdressController adressController;
    private EntityURLBuilder entityURLBuilder;

    private static Adress adressExample = new Adress(1, "Street 1111",
            new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()),
            new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList()));

    private static AdressDto adressDtoExample = AdressDto.builder()
            .id(1)
            .adress("Street 1111")
            .tariff(
                new TariffDto(1, 1123.21f, TariffType.COMMERCIAL))
            .build();

    private static List<Adress> ADRESSES_LIST = List.of(
            new Adress(1, "Street 1111",
                    new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList()),
                    new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList())),
            new Adress(2, "Avenue 2222",
                    new Tariff(2, 222.21f, TariffType.RESIDENTIAL, Collections.emptyList()),
                    new Client(2, "33222111", "Joe", "Doe", "joe1", "2211", Collections.emptyList())));

    /*Result of Adresses list mapped to AdressDto*/
    private static List<AdressDto> ADRESSESDTO_LIST = List.of(
            new AdressDto(1, "Street 1111",
                    new TariffDto(1, 1123.21f, TariffType.COMMERCIAL)),
            new AdressDto(2, "Avenue 2222",
                    new TariffDto(2, 222.21f, TariffType.RESIDENTIAL)));



    @BeforeEach
    public void setUp(){
        adressService = mock(AdressService.class);
        tariffService = mock(TariffService.class);
        conversionService = Mockito.mock(ConversionService.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        adressController = new AdressController(adressService, tariffService, conversionService);
    }

    @Test
    public void testAddAdressCreated(){
        ResponseEntity response = null;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        try {
            when(adressService.add(any(Adress.class))).thenReturn(adressExample);
            response = adressController.addAdress(adressDtoExample);
        } catch (GenericWebException e) {
            e.printStackTrace();
        }
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost/adresses/1", response.getHeaders().getLocation().toString());
    }

    @Test
    public void testAddAdressThrowsException(){
        try {
            when(adressService.add(any(Adress.class))).thenThrow(AdressExistsException.class);
        } catch (AdressExistsException e) {
            e.printStackTrace();
        }
        assertThrows(AdressExistsException.class, ()->
                adressService.add(adressExample));
    }

    @Test
    public void testGetAllAdressesNoContent(){
        //Given

        Pageable pageable = PageRequest.of(50, 10);
        Page<Adress> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(adressService.getAll(pageable)).thenReturn(mockedPage);
        when(mockedPage.map(Mockito.any())).thenReturn(mock(Page.class));

        //Then
        ResponseEntity<List<AdressDto>> response = adressController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllAdressesOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(2L);
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(mockedPage.getContent()).thenReturn(ADRESSES_LIST);
        when(adressService.getAll(pageable)).thenReturn(mockedPage);

        Page mockedPageDto = mock(Page.class);
        when(mockedPageDto.getTotalElements()).thenReturn(2L);
        when(mockedPageDto.getTotalPages()).thenReturn(1);
        when(mockedPageDto.getContent()).thenReturn(ADRESSESDTO_LIST);

        when(mockedPage.map(any())).thenReturn(mockedPageDto);

        //Then
        ResponseEntity<List<AdressDto>> response = adressController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(ADRESSESDTO_LIST, response.getBody());
    }

    @Test
    public void testDeleteAdressOk() throws Exception{
        Mockito.doNothing().when(adressService).deleteById(any(Integer.class));
        ResponseEntity response = adressController.deleteAdress(any(Integer.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateAdressOk() throws Exception{
        Mockito.doNothing().when(adressService).update(any(Integer.class), any(Adress.class));
        ResponseEntity response = adressController.updateAdress(any(Integer.class), any(Adress.class));
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
}
