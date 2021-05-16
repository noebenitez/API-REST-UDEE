package com.utn.udee.service;

import com.utn.udee.repository.AdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdressService {

    @Autowired
    private AdressRepository adressRepository;
}
