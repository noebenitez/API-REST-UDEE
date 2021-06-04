package com.utn.udee.controller;

import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.model.dto.TariffDto;
import com.utn.udee.service.TariffService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TariffControllerTest {

    private TariffService tariffService;
    private ConversionService conversionService;
    private TariffController tariffController;
    private EntityURLBuilder entityURLBuilder;

    private static Tariff tariffExample = new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList());
    private static TariffDto tariffDtoExample = TariffDto.builder()
            .tariff(1123.21f)
            .tariffType(TariffType.COMMERCIAL).build();

    private static List<Tariff> TARIFF_LIST = List.of(
            Tariff.builder().id(1).tariff(1123.21f).tariffType(TariffType.COMMERCIAL).adresses(Collections.emptyList()).build(),
            Tariff.builder().id(2).tariff(222.21f).tariffType(TariffType.RESIDENTIAL).adresses(Collections.emptyList()).build(),
            Tariff.builder().id(3).tariff(333.3f).tariffType(TariffType.SOCIAL).adresses(Collections.emptyList()).build());

    /*Result of Tariff list mapped to TariffDto*/
    private static List<TariffDto> TARIFFDTO_LIST = List.of(
            TariffDto.builder().id(1).tariff(1123.21f).tariffType(TariffType.COMMERCIAL).build(),
            TariffDto.builder().id(2).tariff(222.21f).tariffType(TariffType.RESIDENTIAL).build(),
            TariffDto.builder().id(3).tariff(333.3f).tariffType(TariffType.SOCIAL).build());


    @BeforeEach
    public void setUp(){
        tariffService = mock(TariffService.class);
        conversionService = Mockito.mock(ConversionService.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        tariffController = new TariffController(tariffService, conversionService);
    }
/*
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<TariffDto>> getAll(Pageable pageable) {
        Page page = tariffService.getAll(pageable);
        Page pageDto = page.map(tariff -> conversionService.convert(tariff, TariffDto.class));
        return ResponseEntityMaker.response(pageDto.getContent(), pageDto);

    }*/


    /*Test for scenarios of method ResponseEntity addTariff(@RequestBody Tariff tariff)*/

/*    @Test
    public void testAddTariffCreated() throws Exception {
        //Given
        //final EntityURLBuilder spyEB = Mockito.spy(new EntityURLBuilder());
        Tariff t = Tariff.builder()
                .id(1)
                .tariff(tariffDtoExample.getTariff())
                .tariffType(tariffDtoExample.getTariffType())
                .build();
        when(tariffService.add(t)).thenReturn(t);
        //doReturn(URI.create("http://api/tariff/" + t.getId())).when(entityURLBuilder).buildURL(nullable(String.class), nullable(Integer.class));
        when(entityURLBuilder.buildURL(any(String.class), any(Integer.class))).thenReturn(URI.create("http://api/tariff/" + t.getId()));

        //Then
        ResponseEntity response = tariffController.addTariff(tariffDtoExample);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals("http://api/tariff/" + t.getId() ,response.getHeaders().getLocation());
    }*/


    /*Test for scenarios of method ResponseEntity<List<TariffDto>> getAll(Pageable pageable)*/
    @Test
    public void testAllTariffNoContent(){
        //Given
        TariffDto tariffDto = new TariffDto();

        Pageable pageable = PageRequest.of(50, 10);
        Page<Tariff> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(tariffService.getAll(pageable)).thenReturn(mockedPage);
        Page<TariffDto> mockedPageDto = mock(Page.class);
        when(mockedPage.map(Mockito.any())).thenReturn(mock(Page.class));

        //Then
        ResponseEntity<List<TariffDto>> response = tariffController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testAllTariffOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(3L);
        when(mockedPage.getTotalPages()).thenReturn(4);
        when(mockedPage.getContent()).thenReturn(TARIFF_LIST);
        when(tariffService.getAll(pageable)).thenReturn(mockedPage);

        Page mockedPageDto = mock(Page.class);
        when(mockedPageDto.getTotalElements()).thenReturn(3L);
        when(mockedPageDto.getTotalPages()).thenReturn(4);
        when(mockedPageDto.getContent()).thenReturn(TARIFFDTO_LIST);

        when(mockedPage.map(any())).thenReturn(mockedPageDto);

        //Then
        ResponseEntity<List<TariffDto>> response = tariffController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(4, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(TARIFFDTO_LIST, response.getBody());
    }

    @Test
    public void testDeleteTariffOk() throws Exception{
        Mockito.doNothing().when(tariffService).deleteById(any(Integer.class));
        ResponseEntity response = tariffController.deleteTariff(any(Integer.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateTariffOk() throws Exception{
        Mockito.doNothing().when(tariffService).update(any(Integer.class), any(Tariff.class));
        ResponseEntity response = tariffController.updateTariff(any(Integer.class), any(Tariff.class));
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

}
