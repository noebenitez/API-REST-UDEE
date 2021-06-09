package com.utn.udee.controller;

import com.utn.udee.exception.AdressExistsException;
import com.utn.udee.exception.AdressNotExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.dto.AdressDto;
import com.utn.udee.service.AdressService;
import com.utn.udee.service.TariffService;
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
@RequestMapping(value = "/adresses")
public class AdressController {

    private static final String ADRESS_PATH = "adresses";

    private final AdressService adressService;
    private final TariffService tariffService;
    private final ConversionService conversionService;

    @Autowired
    public AdressController(AdressService adressService, TariffService tariffService, ConversionService conversionService){
        this.adressService = adressService;
        this.tariffService = tariffService;
        this.conversionService = conversionService;
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addAdress(@RequestBody AdressDto adress) throws AdressExistsException, TariffNotExistsException {
        Adress adressConverted = Adress.builder()
                .adress(adress.getAdress())
                .tariff(tariffService.getById(adress.getTariff().getId()))
                .build();
        Adress newAdress = adressService.add(adressConverted);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(ADRESS_PATH, newAdress.getId()))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<AdressDto>> getAll(Pageable pageable){
        Page page = adressService.getAll(pageable);
        Page pageDto = page.map(adress -> conversionService.convert(adress, AdressDto.class));
        return ResponseEntityMaker.response(pageDto.getContent(), pageDto);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAdress(@PathVariable Integer id) throws AdressNotExistsException {
        adressService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity updateAdress(@PathVariable Integer id, @RequestBody Adress newAdress) throws AdressNotExistsException {
        adressService.update(id, newAdress);
        return ResponseEntity.accepted().build();
    }
}
