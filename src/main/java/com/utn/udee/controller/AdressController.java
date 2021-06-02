package com.utn.udee.controller;

import com.utn.udee.exception.AdressExistsException;
import com.utn.udee.exception.AdressNotExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.service.AdressService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AdressService adressService;

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addAdress(@RequestBody Adress adress) throws AdressExistsException {
        Adress newAdress = adressService.add(adress);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(ADRESS_PATH, newAdress.getId()))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<Adress>> getAll(Pageable pageable){
        Page page = adressService.getAll(pageable);
        return ResponseEntityMaker.response(page.getContent(), page);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAdress(@PathVariable Integer id) throws AdressNotExistsException {
        adressService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity updateAdress(@PathVariable Integer id, @RequestBody Adress newAdress) throws AdressExistsException {
        adressService.update(id, newAdress);
        return ResponseEntity.accepted().build();
    }
}
