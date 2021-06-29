package com.utn.udee.controller;

import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.exception.MeterSameAddressExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeterDto;
import com.utn.udee.service.MeasurementService;
import com.utn.udee.service.MeterService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MeterControllerTest {
    private MeterService meterService;
    private MeasurementService measurementService;
    private ConversionService conversionService;
    private MeterController meterController;


@BeforeEach
public void setUp()
{
    meterService = mock(MeterService.class);
    measurementService = mock(MeasurementService.class);
    conversionService = mock(ConversionService.class);
    meterController = new MeterController(meterService,measurementService);
}

@Test
public void testAddMeterOk()
{
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    try {
        ///when
        when(meterService.add(any(Meter.class))).thenReturn(aMeter);
        ResponseEntity response = meterController.addMeter(aMeter);
        ///then
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(EntityURLBuilder.buildURL("meters",aMeter.getId()),response.getHeaders().getLocation());

    } catch (MeterSameAddressExistsException e) {
        Assertions.fail("This test must not throw an exception");
    }
}

@Test
    public void testGetMeterDtoByIdOk()
{
    try {
        when(meterService.getById(anyInt())).thenReturn(aMeter);
        ResponseEntity<MeterDto> response = meterController.getMeterDtoById(IDMETER);
        ///then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(aMeterDto,response.getBody());
    }
    catch (MeterNotExistsException e) {
        Assertions.fail("This test should not throw an exception");
    }


}

@Test
    public void testGetAllOk()
{
    ///when
    when(meterService.getAll(eq(aPageable()))).thenReturn(aMeterPage());
    ///then
    ResponseEntity<Page<Meter>> response = meterController.getAll(aPageable());

    assertEquals(HttpStatus.OK,response.getStatusCode());
    assertEquals(aMeterPage().getContent(),response.getBody());
}

@Test
    public void testDeleteByIdOk()
{
    ResponseEntity response = null;
    try {
        response = meterController.deleteById(IDMETER);

        //then
        verify(meterService,times(1)).deleteById(anyInt());
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());

    } catch (MeterNotExistsException e) {
        Assertions.fail("This test should not throw an exception");
    }


}

@Test
    public void testDeleteByIdNotExists() throws MeterNotExistsException {
    ///when
    doThrow(new MeterNotExistsException()).when(meterService).deleteById(anyInt());
    //then
    assertThrows(MeterNotExistsException.class, () -> {meterController.deleteById(IDMETER);});
}




}