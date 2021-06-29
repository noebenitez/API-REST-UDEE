package com.utn.udee.controller;

import com.utn.udee.controller.converter.MeasurementToMeasurementDTOConverter;
import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.service.MeasurementService;
import com.utn.udee.service.MeterService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MeasurementControllerTest {
    private MeasurementService measurementService;
    private MeterService meterService;
    private ModelMapper modelMapper;
    private ConversionService conversionService;
    private MeasurementController measurementController;


    @BeforeEach
    public void setUp() {

        measurementService = mock(MeasurementService.class);
        meterService = mock(MeterService.class);
        conversionService = mock(ConversionService.class);
        modelMapper = mock(ModelMapper.class);
        measurementController = new MeasurementController(measurementService,meterService,conversionService,modelMapper);


    }

    @Test
    public void testGetById()
    {
        try {
            when(measurementService.getById(anyInt())).thenReturn(aMeasurement);
           when(modelMapper.map(any(),eq(MeasurementDto.class))).thenReturn(aMeasurementDto);

            ResponseEntity<MeasurementDto> response = measurementController.getById(IDMEASUREMENT);

            ///then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(aMeasurementDto,response.getBody());
        } catch (MeasurementNotExistsException e) {
            Assertions.fail("this should not throw an exception");
        }
    }

    @Test
    public void testGetByIdNotExists() throws MeasurementNotExistsException {
        //given
        doThrow(new MeasurementNotExistsException()).when(measurementService).getById(anyInt());
        //then
        assertThrows(MeasurementNotExistsException.class, () -> { measurementController.getById(IDMEASUREMENT);});
    }

    @Test
    public void addMeasurementOk()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ///when
        try {
            when(meterService.getbySerialNumber(anyString())).thenReturn(aMeter);
            when(measurementService.add(aMeasurementSenderDto,aMeter)).thenReturn(aMeasurement);
            ResponseEntity response = measurementController.addMeasurement(aMeasurementSenderDto);
        ///then
            assertEquals(HttpStatus.CREATED,response.getStatusCode());
            assertEquals(EntityURLBuilder.buildURL("measurements",aMeasurement.getId()),response.getHeaders().getLocation());

        } catch (MeterNotExistsException e) {
            Assertions.fail("This test should not throw an exception");
        }
    }

/*    @Test
    public void testGetAllOk()
    {
        //when
        when(measurementService.getAllMeasurements(aPageable())).thenReturn(aMeasurementPage());
        when(aMeasurementPage().map(measurement -> conversionService.convert(measurement,MeasurementDto.class))).thenReturn(aMeasurementDtoPage());
        ResponseEntity<List<MeasurementDto>> response = measurementController.getAll(aPageable());
        ///then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(aMeasurementDtoList,response.getBody());

    }*/







}
