package com.utn.udee.service;

import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.Address;
import com.utn.udee.model.Client;
import com.utn.udee.model.Invoice;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.InvoiceDto;
import com.utn.udee.repository.InvoiceRepository;
import com.utn.udee.utils.ListMapperDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {
    private ModelMapper modelMapper;
    private final InvoiceRepository invoiceRepository;
    private UserService userService;
    private AddressService addressService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository,ModelMapper modelMapper,UserService userService, AddressService addressService) {
        this.invoiceRepository = invoiceRepository;
        this.modelMapper=modelMapper;
        this.userService=userService;
        this.addressService=addressService;
    }


    public List<InvoiceDto> getInvoicesByRangeDate(Integer userId, LocalDateTime from, LocalDateTime to) {
        return ListMapperDto.getListDto(modelMapper,invoiceRepository.getInvoicesByRangeDate(userId,from,to),InvoiceDto.class);
    }

    public Page<Invoice> getUnpaidInvoices(Integer userId, Pageable pageable) throws UserNotExistsException {
        Client u = (Client)userService.getById(userId);
       return invoiceRepository.findAllByBilledToAndNotPaid(userId,pageable);
    }

    public Page<Invoice> getUnpaidInvoicesByUserAndAddress(Integer userId, Integer addressId, Pageable pageable) throws UserNotExistsException, AddressNotExistsException {
        Client u = (Client) userService.getById(userId);
        Address a = addressService.getById(addressId);
        return invoiceRepository.FindUnpaidInvoicesByUserAndAddress(userId,addressId,pageable);

    }
}
