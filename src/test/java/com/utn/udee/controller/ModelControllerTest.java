package com.utn.udee.controller;

import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.exception.ModelNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.model.Model;
import com.utn.udee.model.dto.ModelDto;
import com.utn.udee.service.ModelService;
import com.utn.udee.utils.EntityURLBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ModelControllerTest {

    private ModelService modelService;
    private ModelController modelController;

    @BeforeEach
    public void SetUp()
    {
        modelService = mock(ModelService.class);
        modelController = new ModelController(modelService);
    }

    @Test
    public void testGetByIdOk() {
        try {
            when(modelService.getById(anyInt())).thenReturn(aModel);
            ResponseEntity<ModelDto> response= modelController.getById(IDMODEL);
            ///then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(aModelDto,response.getBody());

        } catch (ModelNotExistsException e) {
            Assertions.fail("This test should not throw an exception");
        }
    }

    @Test
    public void testAddModelOk()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(modelService.add(any(Model.class))).thenReturn(aModel);
        ResponseEntity response = modelController.addModel(aModel);
        ///then

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(EntityURLBuilder.buildURL("models",aModel.getId()),response.getHeaders().getLocation());
    }

    @Test
    public void testGetByIdNotExists() throws ModelNotExistsException {
        doThrow(new ModelNotExistsException()).when(modelService).getById(anyInt());

        assertThrows(ModelNotExistsException.class, () -> { modelController.getById(IDMODEL);});
    }

}
