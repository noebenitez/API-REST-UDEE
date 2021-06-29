package com.utn.udee.service;

import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.Client;
import com.utn.udee.model.Invoice;
import com.utn.udee.repository.InvoiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import static com.utn.udee.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class invoiceServiceTest {

    private InvoiceService invoiceService;
    private InvoiceRepository invoiceRepository;
    private ModelMapper modelMapper;
    private UserService userService;
    private AddressService addressService;

    @BeforeEach
    public void SetUp()
    {
        invoiceRepository = mock(InvoiceRepository.class);
        modelMapper = mock(ModelMapper.class);
        userService = mock(UserService.class);
        addressService = mock(AddressService.class);
        invoiceService = new InvoiceService(invoiceRepository,modelMapper,userService,addressService);
    }

    @Test
    public void testGetUnpaidInvoicesOk()
    {
        try {
            when((Client)userService.getById(anyInt())).thenReturn((Client)aUserClient);
            when(invoiceRepository.findAllByBilledToAndNotPaid(anyInt(),eq(aPageable()))).thenReturn(anInvoicePage());
            Page<Invoice> p = invoiceService.getUnpaidInvoices(IDUSER,aPageable());
            assertEquals(p.getContent(),anInvoicePage().getContent());
            assertEquals(p.getSize(),anInvoicePage().getSize());

        } catch (UserNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }

    }

    @Test
    public void testGetUnpaidInvoicesNotExists() throws UserNotExistsException {
        when((Client)userService.getById(anyInt())).thenThrow(UserNotExistsException.class);
        assertThrows(UserNotExistsException.class,()->{invoiceService.getUnpaidInvoices(IDUSER,aPageable());});
    }

    @Test
    public void testGetUnpaidInvoicesByUserAndAddressOk(){
        try {
            when(userService.getById(anyInt())).thenReturn(aUserClient);
            when(addressService.getById(anyInt())).thenReturn(anAddress);
            when(invoiceRepository.FindUnpaidInvoicesByUserAndAddress(anyInt(),anyInt(),eq(aPageable()))).thenReturn(anInvoicePage());
            Page<Invoice> p = invoiceService.getUnpaidInvoicesByUserAndAddress(IDUSER,IDADDRESS,aPageable());
            assertEquals(anInvoicePage().getContent().get(0).getId(),p.getContent().get(0).getId());
            assertEquals(anInvoicePage().getSize(),p.getSize());
        } catch (UserNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        } catch (AddressNotExistsException e) {
            Assertions.fail("This should not throw an exception");
        }

    }

    @Test
    public void testUnpaidInvoicesByUserAndAddressNotExistsAddress() throws AddressNotExistsException {
        when(addressService.getById(anyInt())).thenThrow(AddressNotExistsException.class);
        assertThrows(AddressNotExistsException.class,()->{invoiceService.getUnpaidInvoicesByUserAndAddress(IDUSER,IDADDRESS,aPageable());});

    }

    @Test
    public void testUnpaidInvoicesByUserAndAddressNotExistsUser() throws UserNotExistsException {
        when(userService.getById(anyInt())).thenThrow(UserNotExistsException.class);
        assertThrows(UserNotExistsException.class,()->{invoiceService.getUnpaidInvoicesByUserAndAddress(IDUSER,IDADDRESS,aPageable());});


    }

}