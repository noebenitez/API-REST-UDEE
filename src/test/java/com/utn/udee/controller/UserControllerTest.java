package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.controller.converter.UserToUserDTOConverter;
import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.FailTokenException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.*;
import com.utn.udee.model.projections.ConsumptionProjection;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.service.InvoiceService;
import com.utn.udee.service.TariffService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ListMapperDto;
import com.utn.udee.utils.ResponseEntityMaker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private UserProjection aUserProjection;
    private ConsumptionProjection aConsumptionProjection;
    private ObjectMapper objectMapper;
    private UserToUserDTOConverter userDTOConverter;
    private UserController userController;
    private EntityURLBuilder entityURLBuilder;
    private ModelMapper modelMapper;
    private InvoiceService invoiceService;
    private ConversionService conversionService;
    private static List<UserProjection> aUserProjectionList;

    @BeforeEach
    public void setUp(){
        userService = mock(UserService.class);
        objectMapper = mock(ObjectMapper.class);
        userDTOConverter = Mockito.mock(UserToUserDTOConverter.class);
        entityURLBuilder = Mockito.mock(EntityURLBuilder.class);
        modelMapper = mock(ModelMapper.class);
        invoiceService =  mock(InvoiceService.class);
        conversionService = mock(ConversionService.class);
        userController = new UserController(userService, objectMapper,modelMapper,userDTOConverter,invoiceService, conversionService);

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        aUserProjection = factory.createProjection(UserProjection.class);
        aUserProjection.setUsername("NN");
        aUserProjection.setFirstname("NNN");
        aUserProjection.setLastname("MM");
        aUserProjection.setSum(2.5f);
        aUserProjection.setDni("1234");

        aUserProjectionList = List.of(aUserProjection);

        aConsumptionProjection = factory.createProjection(ConsumptionProjection.class);
        aConsumptionProjection.setTotalAmount(1000.f);
        aConsumptionProjection.setTotalKw(2000.f);
    }

    @Test
    public void testGetAllUsersNoContent(){
        //Given

        Pageable pageable = PageRequest.of(50, 10);
        Page<UserDtoI> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(userService.getAll(pageable)).thenReturn(mockedPage);

        //Then
        ResponseEntity<List<UserDtoI>> response = userController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllUsersOk(){
        //Given
        Pageable pageable = PageRequest.of(1, 2);

        Page mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(2L);
        when(mockedPage.getTotalPages()).thenReturn(1);

        when(mockedPage.getContent()).thenReturn(aUsersDtoList);
        when(userService.getAll(pageable)).thenReturn(mockedPage);

        //Then
        ResponseEntity<List<UserDtoI>> response = userController.getAll(pageable);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, Long.parseLong(response.getHeaders().get("X-Total-Count").get(0)) );
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)) );
        assertEquals(aUsersDtoList, response.getBody());
    }

    @Test
    public void testLoginUnauthorized() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto("notAUser", "3322");
        when(userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword())).thenReturn(null);

        ResponseEntity<LoginResponseDto> response = userController.login(loginRequestDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetTop10ConsumersOk()
    {
        ///when
            when(userService.getTop10Consumers(any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(List.of(aUserProjection));
            ResponseEntity<List<UserProjection>> response = userController.getTop10Consumers(LocalDateTime.now(),LocalDateTime.now());
       ///then
         assertEquals(HttpStatus.OK,response.getStatusCode());
         assertEquals(aUserProjectionList,response.getBody());
    }

    @Test
    public void testGetTotalRangeDateConsumptionOk() {
        ///when
        when(userService.getTotalRangeDateConsumption(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aConsumptionProjection);
        ResponseEntity<ConsumptionProjection> response;
        try {
            response = userController.getTotalRangeDateConsumption(IDUSER,LocalDateTime.now(),LocalDateTime.now());

            ///then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(aConsumptionProjection,response.getBody());

        } catch (UserNotExistsException e) {
            Assertions.fail("It must not throw an exception");
        }
    }

    @Test
    public void testGetRangeDateConsumptionOk()
    {
        //when
        when(userService.getRangeDateConsumption(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
        when(ListMapperDto.getListDto(modelMapper,aMeasurementList,MeasurementDto.class)).thenReturn(aMeasurementDtoList);

        try {
            ResponseEntity<List<MeasurementDto>> response = userController.getRangeDateConsumption(IDUSER,LocalDateTime.now(),LocalDateTime.now());

            //then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(1,response.getBody().size());
        } catch (UserNotExistsException e) {
            Assertions.fail("It must not throw an exception");

        }
    }

    @Test
    public void TestGetMeasurementsAddressDateRange()  {

        try {
            //when
            when(userService.getMeasurementsFromDateRange(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(aMeasurementList);
            when(ListMapperDto.getListDto(modelMapper,aMeasurementList,MeasurementDto.class)).thenReturn(aMeasurementDtoList);
            ResponseEntity<List<MeasurementDto>> response = userController.getMeasurementsAddressDateRange(IDADDRESS,LocalDateTime.now(),LocalDateTime.now());

            //then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(1,response.getBody().size());
        } catch (AddressNotExistsException e) {
            Assertions.fail("It must not throw an exception");
        }

    }

    @Test
    public void testGetInvoicesByRangeDate()
    {
        ///given
        when(invoiceService.getInvoicesByRangeDate(anyInt(),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(anInvoiceDtoList);
        try {
            ///when
            ResponseEntity<List<InvoiceDto>> response = userController.getInvoicesByRangeDate(IDUSER,LocalDateTime.now(),LocalDateTime.now());
            //then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(anInvoiceDtoList,response.getBody());
            assertEquals(1,response.getBody().size());
        } catch (UserNotExistsException e) {
            Assertions.fail("It must now throw an exception");
        }
    }

    @Test
    public void testGetUnpaidInvoicesByUserAndAddress()
    {
        try {
            ///when
            when(invoiceService.getUnpaidInvoicesByUserAndAddress(anyInt(),anyInt(),eq(aPageable()))).thenReturn(anInvoicePage());
            when(anInvoicePage().map(invoice -> conversionService.convert(invoice,InvoiceDto.class))).thenReturn(anInvoiceDtoPage());
            ///then
            ResponseEntity<List<InvoiceDto>> response = userController.getUnpaidInvoicesByUserAndAddress(IDUSER,IDADDRESS,aPageable());
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(anInvoiceDtoPage(),response.getBody().get(0));
            assertEquals(anInvoiceDtoPage().getSize(),response.getBody().size());
        } catch (UserNotExistsException e) {
            Assertions.fail("It must not throw this exception");
        } catch (AddressNotExistsException e) {
            Assertions.fail("It must not throw this exception");
        }
    }

    @Test
    public void getUnpaidInvoices()
    {
        ///when
        try {
            when(invoiceService.getUnpaidInvoices(anyInt(),eq(aPageable()))).thenReturn(anInvoicePage());
            when(anInvoicePage().map(invoice -> conversionService.convert(invoice,InvoiceDto.class))).thenReturn(anInvoiceDtoPage());
            ResponseEntity<List<InvoiceDto>> response = userController.getUnpaidInvoices(IDUSER,aPageable());
            ///then
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(anInvoiceDtoPage(),response.getBody().get(0));
            assertEquals(anInvoiceDtoPage().getSize(),response.getBody().size());

        } catch (UserNotExistsException e) {
            Assertions.fail("This test must not throw an exception");
        }


    }












}
