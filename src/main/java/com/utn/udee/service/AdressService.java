package com.utn.udee.service;

import com.utn.udee.model.Adress;
import com.utn.udee.repository.AdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdressService {

    @Autowired
    private AdressRepository adressRepository;

    public List<Adress> getAll() {
        return adressRepository.findAll();
    }

    public void add(Adress adress) {
        adressRepository.save(adress);
    }
}
