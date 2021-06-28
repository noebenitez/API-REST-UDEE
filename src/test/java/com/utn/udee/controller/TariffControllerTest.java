package com.utn.udee.controller;

import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.model.dto.TariffDto;
import com.utn.udee.service.TariffService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

public class TariffControllerTest {

    private TariffService tariffService;
    private ConversionService conversionService;
    private TariffController tariffController;
    private EntityURLBuilder entityURLBuilder;

    @BeforeEach
    public void setUp(){
        tariffService = mock(TariffService.class);
        conversionService = Mockito.mock(ConversionService.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        tariffController = new TariffController(tariffService, conversionService);
    }

    @Test
    public void testAddTariffCreated(){
        ResponseEntity response = null;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

       try {
            when(tariffService.add(any(Tariff.class))).thenReturn(aTariff);
            response = tariffController.addTariff(aTariffDto);
        } catch (TariffExistsException e) {
            e.printStackTrace();
        }
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost/tariffs/1", response.getHeaders().getLocation().toString());
    }

    @Test
    public void testAddTariffThrowsException(){
        try {
            when(tariffService.add(any(Tariff.class))).thenThrow(TariffExistsException.class);
        } catch (TariffExistsException e) {
            e.printStackTrace();
        }
        assertThrows(TariffExistsException.class, ()->
                tariffController.addTariff(aTariffDto));
    }

    @Test
    public void testGetAllTariffsNoContent(){
        //Given

        Pageable pageable = PageRequest.of(50, 10);
        Page<Tariff> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(tariffService.getAll(pageable)).thenReturn(mockedPage);
        when(mockedPage.map(Mockito.any())).thenReturn(mock(Page.class));

        //Then
        ResponseEntity<List<TariffDto>> response = tariffController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllTariffsOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(3L);
        when(mockedPage.getTotalPages()).thenReturn(4);
        when(mockedPage.getContent()).thenReturn(aTariffList);
        when(tariffService.getAll(pageable)).thenReturn(mockedPage);

        Page mockedPageDto = mock(Page.class);
        when(mockedPageDto.getTotalElements()).thenReturn(3L);
        when(mockedPageDto.getTotalPages()).thenReturn(4);
        when(mockedPageDto.getContent()).thenReturn(aTariffDtoList);
        when(mockedPage.map(any())).thenReturn(mockedPageDto);

        //Then
        ResponseEntity<List<TariffDto>> response = tariffController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(4, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(aTariffDtoList, response.getBody());
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
