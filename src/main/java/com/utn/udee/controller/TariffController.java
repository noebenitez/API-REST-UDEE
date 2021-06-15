package com.utn.udee.controller;
import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Tariff;
import com.utn.udee.model.dto.TariffDto;
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
@RequestMapping(value = "/tariffs")
public class TariffController {

    private static final String TARIFF_PATH = "tariffs";

    private final TariffService tariffService;
    private final ConversionService conversionService;

    @Autowired
    public TariffController(TariffService tariffService, ConversionService conversionService) {
        this.tariffService = tariffService;
        this.conversionService = conversionService;
    }


    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addTariff(@RequestBody TariffDto tariff) throws TariffExistsException {
        Tariff tariffConverted = Tariff.builder()
                .tariff(tariff.getTariff())
                .tariffType(tariff.getTariffType())
                .build();
        Tariff newTariff = tariffService.add(tariffConverted);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(TARIFF_PATH, newTariff.getId()))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<TariffDto>> getAll(Pageable pageable) {
        Page page = tariffService.getAll(pageable);
        Page pageDto = page.map(tariff -> conversionService.convert(tariff, TariffDto.class));
        return ResponseEntityMaker.response(pageDto.getContent(), pageDto);

    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTariff(@PathVariable Integer id) throws TariffNotExistsException {
        tariffService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity updateTariff(@PathVariable Integer id, @RequestBody Tariff newTariff) throws TariffNotExistsException {
        tariffService.update(id, newTariff);
        return ResponseEntity.accepted().build();
    }
}
