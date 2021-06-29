package com.utn.udee.service;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.repository.MeasurementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeasurementServiceTest {
    private MeasurementRepository measurementRepository;
    private MeasurementService measurementService;
    private UserProjection aUserProjection;
    private List<UserProjection> aUserProjectionList;


    @BeforeEach
    public void SetUp()
    {
        measurementRepository = mock(MeasurementRepository.class);
        measurementService = new MeasurementService(measurementRepository);
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
    public void testGetByIdOk()
    {
        when(measurementRepository.findById(anyInt())).thenReturn(Optional.of(aMeasurement));
        try {
            Measurement m = measurementService.getById(IDMEASUREMENT);
            //then
            assertEquals(aMeasurement.getId(),m.getId());

        } catch (MeasurementNotExistsException e) {
            Assertions.fail("This test should not throw an exception");
        }

    }

    @Test
    public void testGetMeasurementsByMeterOk()
    {
        when(measurementRepository.findMeasurementByMeterId(anyInt(),eq(aPageable()))).thenReturn(aMeasurementPage());
        Page<Measurement> p = measurementService.getMeasurementsByMeter(IDMETER,aPageable());
        ///then
        assertEquals( aMeasurementPage().getContent(),p.getContent());

    }

    @Test
    public void testGetAllMeasurementsOk()
    {
        when(measurementRepository.findAll(eq(aPageable()))).thenReturn(aMeasurementPage());
        Page<Measurement> p = measurementService.getAllMeasurements(aPageable());
        ///then

        assertEquals(aMeasurementPage().getContent(),p.getContent());
        assertEquals(aMeasurementPage().getSize(),p.getSize());
    }

    @Test
    public void testGetAllMeasurementsEmpty()
    {
        when(measurementRepository.findAll((eq(aPageable())))).thenReturn(Page.empty(aPageable()));
        Page<Measurement> p = measurementService.getAllMeasurements(aPageable());
        ///then

        assertEquals(Page.empty(aPageable()).getContent(),p.getContent());
        assertEquals(Page.empty(aPageable()).getSize(),p.getSize());
    }

    @Test
    public void testGetMeasurementsByAddressRangeDateOk()
    {
        when(measurementRepository.getByAddressAndRangeDate(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
        List<Measurement> measurements = measurementService.getMeasurementsByAddressRangeDate(IDADDRESS,LocalDateTime.now(),LocalDateTime.now());

        ///then
        assertEquals(aMeasurementList.get(0).getId(),measurements.get(0).getId());
        assertEquals(aMeasurementList.size(),measurements.size());
    }
@Test
    public void testGetTop10ConsumersOk()
    {
        when(measurementRepository.getTop10Consumers(any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aUserProjectionList);
        List<UserProjection> list = measurementService.getTop10Consumers(LocalDateTime.now(),LocalDateTime.now());
        ///then

        assertEquals(aUserProjectionList.get(0).getDni(),list.get(0).getDni());
        assertEquals(aUserProjectionList.size(),list.size());
    }

    @Test
    public void testGetRangeDateConsumptionOk()
    {
        when(measurementRepository.getConsumptionByRangeDate(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
        List<Measurement> list = measurementService.getRangeDateConsumption(IDUSER,LocalDateTime.now(),LocalDateTime.now());
        ///then

        assertEquals(aMeasurementList.get(0).getId(),list.get(0).getId());
        assertEquals(aMeasurementList.size(),list.size());
    }















}
