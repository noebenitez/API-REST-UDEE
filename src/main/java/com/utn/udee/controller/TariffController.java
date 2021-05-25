package com.utn.udee.controller;

import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.model.Tariff;
import com.utn.udee.service.TariffService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/tariffs")
public class TariffController {

    private static final String TARIFF_PATH = "tariffs";

    @Autowired
    private TariffService tariffService;

    @PostMapping
    public ResponseEntity addTariff(@RequestBody Tariff tariff) throws TariffExistsException {
        Tariff newTariff = tariffService.add(tariff);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(TARIFF_PATH, newTariff.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Tariff>> getAll(Pageable pageable){
        Page page = tariffService.getAll(pageable);
        return ResponseEntityMaker.response(page.getContent(), page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTariff(@PathVariable Integer id){
        tariffService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTariff(@PathVariable Integer id, @RequestBody Tariff newTariff) throws TariffExistsException {
       Tariff tariff = tariffService.getById(id);
       tariff.setTariff(newTariff.getTariff());
       tariff.setTariffType(newTariff.getTariffType());
       tariff.setAdresses(newTariff.getAdresses());
       tariffService.add(tariff);
       return ResponseEntity.accepted().build();
    }

}
