package com.utn.udee.service;

import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.exception.MeterSameAddressExistsException;
import com.utn.udee.model.Meter;
import com.utn.udee.repository.MeterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static com.utn.udee.utils.TestUtils.*;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MeterServiceTest {
    private MeterRepository meterRepository;
    private MeterService meterService;

    @BeforeEach
    public void SetUp()
    {
        meterRepository = mock(MeterRepository.class);
        meterService = new MeterService(meterRepository);
    }

/*    @Test
    public void addOk() {

        when(meterRepository.findMeterByAddress_Id(anyInt())).thenReturn(Optional.empty());
        //when(meterRepository.save(any(Meter.class))).thenReturn(aMeter);
        Meter m = null;
        try {
            m = meterService.add(aMeter);
            assertEquals(aMeasurement.getId(), m.getId());
        } catch (MeterSameAddressExistsException e) {
            Assertions.fail("This should not throw an exception");
        }
    }*/

    @Test
    public void testGetByIdOk()
    {
        try {
            when(meterRepository.findById(anyInt())).thenReturn(Optional.of(aMeter));
            Meter m = meterService.getById(IDMETER);
            assertEquals(aMeter.getId(),m.getId());
            assertEquals(aMeter.getSerialNumber(),m.getSerialNumber());
        } catch (MeterNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }

    }

    @Test
    public void testGetByIdNotExists()
    {
        when(meterRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(MeterNotExistsException.class,()-> { meterService.getById(IDMETER);});
    }

    @Test
    public void testGetAllOk()
    {
        when(meterRepository.findAll(eq(aPageable()))).thenReturn(aMeterPage());
        Page<Meter> meters = meterService.getAll(aPageable());
        assertEquals(aMeterPage().getContent().get(0).getId(),meters.getContent().get(0).getId());
        assertEquals(aMeterPage().getSize(),meters.getSize());

    }

    @Test
    public void testGetAllEmpty()
    {
        when(meterRepository.findAll(eq(aPageable()))).thenReturn(Page.empty());
        Page<Meter> meters = meterService.getAll(aPageable());
        assertEquals(Page.empty(),meters);
        assertEquals(Page.empty().getSize(),meters.getSize());
    }

    @Test
    public void deleteByIdOk()
    {
        when(meterRepository.findById(anyInt())).thenReturn(Optional.of(aMeter));
        try {
            meterService.deleteById(IDMETER);
            verify(meterRepository,times(1)).deleteById(anyInt());
        } catch (MeterNotExistsException e) {
            Assertions.fail("It should not throw an exception");
        }

    }

    @Test
    public void testDeleteByIdNotExists()
    {
        when(meterRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(MeterNotExistsException.class,()-> {meterService.deleteById(IDMETER);});
    }

    @Test
    public void testUpdateMeterOk()
    {
        Meter m = new Meter();
        m.setSerialNumber("1111");
        when(meterRepository.findById(anyInt())).thenReturn(Optional.of(aMeter));

        try {
            meterService.updateMeter(aMeter.getId(),m);
            verify(meterRepository,times(1)).save(aMeter);
        } catch (MeterNotExistsException e) {
            Assertions.fail("It should not throw this exception");
        }
    }

    @Test
    public void updateMeterNotExists()
    {
        when(meterRepository.findById(anyInt())).thenReturn(Optional.empty());
        ///then
        assertThrows(MeterNotExistsException.class,()->{meterService.updateMeter(aMeter.getId(),aMeter);});
    }







}
