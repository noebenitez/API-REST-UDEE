package com.utn.udee.controller;

import com.utn.udee.model.Adress;
import com.utn.udee.service.AdressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/adresses")
public class AdressController {

    @Autowired
    private AdressService adressService;

    @PostMapping
    public void addAdress(@RequestBody Adress adress){
        adressService.add(adress);
    }

    @GetMapping
    public List<Adress> getAll(){
        return adressService.getAll();
    }
}
