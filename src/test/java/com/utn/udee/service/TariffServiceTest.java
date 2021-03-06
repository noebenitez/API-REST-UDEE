package com.utn.udee.service;

import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import com.utn.udee.repository.TariffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.aTariff;
import static com.utn.udee.utils.TestUtils.aTariffList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TariffServiceTest {

    private TariffRepository tariffRepository;
    private TariffService tariffService;

    @BeforeEach
    public void setUp(){
        tariffRepository = mock(TariffRepository.class);
        tariffService = new TariffService(tariffRepository);
    }


    @Test
    public void testAddTariffOk(){
        Tariff toAdd = Tariff.builder().tariff(1123.21f).tariffType(TariffType.COMMERCIAL).build();
        Tariff newTariff = new Tariff();
        when(tariffRepository.existsTariffByTariffAndTariffType(toAdd.getTariff(), toAdd.getTariffType())).thenReturn(false);
        when(tariffRepository.save(any(Tariff.class))).thenReturn(aTariff);
        try {
             newTariff = tariffService.add(toAdd);
        } catch (TariffExistsException e) {
            e.printStackTrace();
        }
        assertNotNull(newTariff.getId());
        assertEquals(toAdd.getTariff(), newTariff.getTariff());
        assertEquals(toAdd.getTariffType(), newTariff.getTariffType());
    }

    @Test   //Trying to add a tariff with same tariff and tariffType
    public void testAddTariffThrowsException(){
        when(tariffRepository.existsTariffByTariffAndTariffType(any(Float.class), any(TariffType.class))).thenReturn(true);
        assertThrows(TariffExistsException.class, ()->
                tariffService.add(aTariff));
    }

    @Test
    public void testGetAllTariffs(){
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(3L);
        when(mockedPage.getTotalPages()).thenReturn(4);
        when(mockedPage.getContent()).thenReturn(aTariffList);
        when(tariffRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Tariff> tariffPage = tariffService.getAll(pageable);

        assertEquals(3L, tariffPage.getTotalElements());
        assertEquals(4, tariffPage.getTotalPages());
        assertEquals(aTariffList, tariffPage.getContent());
    }

    @Test
    public void testDeleteTariffByIdHappyPath(){
        when(tariffRepository.existsById(1)).thenReturn(true);

        try {
            tariffService.deleteById(1);
        } catch (TariffNotExistsException e) {
            e.printStackTrace();
        }

        verify(tariffRepository,times(1)).deleteById(1);
    }

    @Test
    public void testDeleteTariffThowsException(){
        when(tariffRepository.existsById(4)).thenReturn(false);

        assertThrows(TariffNotExistsException.class, ()->
                tariffService.deleteById(4));
    }

    @Test
    public void testGetTariffByIdHappyPath(){
        Tariff tariff = new Tariff();
        when(tariffRepository.findById(1)).thenReturn(Optional.ofNullable(aTariff));
        try {
            tariff = tariffService.getById(1);
        } catch (TariffNotExistsException e) {
            assertEquals(1, tariff.getId());
        }
    }

    @Test
    public void testGetTariffByIdThrowsException(){

        when(tariffRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(TariffNotExistsException.class, ()->
                tariffService.getById(4));
    }

    @Test
    public void testUpdateTariffHappyPath(){
        TariffService mockedService = mock(TariffService.class);
        Tariff newTariff = Tariff.builder()
                .tariff(777f)
                .tariffType(TariffType.RESIDENTIAL).build();
        try {
            when(tariffRepository.findById(1)).thenReturn(Optional.ofNullable(aTariff));
            when(mockedService.getById(1)).thenReturn(aTariff);
            tariffService.update(1, newTariff);
        } catch (TariffNotExistsException e) {
            e.printStackTrace();
        }
        verify(tariffRepository,times(1)).save(aTariff);
    }

    @Test
    public void testUpdateTariffException(){
        TariffService mockedService = mock(TariffService.class);
        try {
            when(mockedService.getById(4)).thenReturn(null);
        } catch (TariffNotExistsException e) {
            e.printStackTrace();
        }
        assertThrows(TariffNotExistsException.class, ()->
                tariffService.update(4, aTariff));
    }
}
