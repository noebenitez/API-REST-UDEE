package com.utn.udee.controller;


import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.model.dto.BrandDto;
import com.utn.udee.service.BrandService;
import com.utn.udee.utils.EntityURLBuilder;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BrandControllerTest {
    private BrandController brandController;
    private BrandService brandService;


    @BeforeEach
    public void setUp() {
        brandService = mock(BrandService.class);
        brandController = new BrandController(brandService);

    }

@Test
    public void testAddBrandOk()
{
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(brandService.add(any(Brand.class))).thenReturn(aBrand);
    ResponseEntity response = brandController.addBrand(aBrand);
    ///then

    assertEquals(HttpStatus.CREATED,response.getStatusCode());
    assertEquals(EntityURLBuilder.buildURL("brands",aBrand.getId()),response.getHeaders().getLocation());
}

@Test
    public void testGetByIdOk()
{
    try {
        when(brandService.getById(anyInt())).thenReturn(aBrand);
        ResponseEntity<BrandDto> response = brandController.getById(IDBRAND);
        ///then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(aBrandDto,response.getBody());
    } catch (BrandNotExistsException e) {
        Assertions.fail("This test should not throw an exception");
    }
}

@Test
    public void testGetByIdNotExists() throws BrandNotExistsException {
    doThrow(new BrandNotExistsException()).when(brandService).getById(anyInt());

    assertThrows(BrandNotExistsException.class, () -> { brandController.getById(IDBRAND);});
}

}