package com.utn.udee.service;


import com.utn.udee.exception.ModelNotExistsException;
import com.utn.udee.model.Model;
import com.utn.udee.repository.ModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ModelServiceTest {

    private ModelRepository modelRepository;
    private ModelService modelService;

    @BeforeEach
    public void SetUp()
    {
        modelRepository = mock(ModelRepository.class);
        modelService = new ModelService(modelRepository);
    }

    @Test
    public void testAddOk()
    {
        when(modelRepository.save(any(Model.class))).thenReturn(aModel);
        modelService.add(aModel);
        verify(modelRepository,times(1)).save(aModel);
    }

    @Test
    public void testGetByIdOk()
    {
        try {
            when(modelRepository.findById(anyInt())).thenReturn(Optional.of(aModel));
            Model m = modelService.getById(aModel.getId());
            assertEquals(aModel.getId(),m.getId());

        } catch (ModelNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }
    }

    @Test
    public void testGetByIdNotExists()
    {
        when(modelRepository.findById(anyInt())).thenReturn(Optional.empty());
        ///then
        assertThrows(ModelNotExistsException.class,()->{ modelService.getById(IDMODEL);});
    }

    @Test
    public void testDeleteByIdOk()
    {
        modelService.deleteById(aModel.getId());
        ///then
        verify(modelRepository,times(1)).deleteById(aModel.getId());
    }
}
