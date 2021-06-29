package com.utn.udee.service;

import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static com.utn.udee.utils.TestUtils.IDBRAND;
import static com.utn.udee.utils.TestUtils.aBrand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BrandServiceTest {

    private BrandRepository brandRepository;
    private BrandService brandService;

    @BeforeEach
    public void SetUp()
    {
        brandRepository = mock(BrandRepository.class);
        brandService = new BrandService(brandRepository);
    }

    @Test
    public void testAddOk()
    {
        when(brandRepository.save(any(Brand.class))).thenReturn(aBrand);
        brandService.add(aBrand);
        verify(brandRepository,times(1)).save(aBrand);
    }

    @Test
    public void testGetByIdOk()
    {
        try {
            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(aBrand));
            Brand b = brandService.getById(aBrand.getId());
            assertEquals(aBrand.getId(),b.getId());

        } catch (BrandNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }
    }

    @Test
    public void testGetByIdNotExists()
    {
        when(brandRepository.findById(anyInt())).thenReturn(Optional.empty());
        ///then
        assertThrows(BrandNotExistsException.class,()->{ brandService.getById(IDBRAND);});
    }

    @Test
    public void testDeleteByIdOk()
    {
        brandService.deleteById(aBrand.getId());
        ///then
        verify(brandRepository,times(1)).deleteById(aBrand.getId());
    }
}
