package com.utn.udee.controller;

import com.utn.udee.exception.*;
import com.utn.udee.model.Address;
import com.utn.udee.model.Client;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.AddressDto;
import com.utn.udee.service.AddressService;
import com.utn.udee.service.TariffService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/addresses")
public class AddressController {

    private static final String ADDRESS_PATH = "addresses";

    private final AddressService addressService;
    private final TariffService tariffService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public AddressController(AddressService addressService, TariffService tariffService, UserService userService, ConversionService conversionService){
        this.addressService = addressService;
        this.tariffService = tariffService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addAddress(@RequestBody AddressDto address) throws AddressExistsException, TariffNotExistsException, UserNotExistsException, IncorrectUserTypeException {
        User client = userService.getById(address.getCustomer().getId());
        if (!(client instanceof Client)){
            throw new IncorrectUserTypeException();
        }
        Address addressConverted = Address.builder()
                .address(address.getAddress())
                .tariff(tariffService.getById(address.getTariff().getId()))
                .customer((Client) client)
                .build();
        Address newAddress = addressService.add(addressConverted);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(ADDRESS_PATH, newAddress.getId()))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAll(Pageable pageable){
        Page page = addressService.getAll(pageable);
        Page pageDto = page.map(adress -> conversionService.convert(adress, AddressDto.class));
        return ResponseEntityMaker.response(pageDto.getContent(), pageDto);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable Integer id) throws AddressNotExistsException {
        addressService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity updateAddress(@PathVariable Integer id, @RequestBody Address newAddress) throws AddressNotExistsException {
        addressService.update(id, newAddress);
        return ResponseEntity.accepted().build();
    }
}
