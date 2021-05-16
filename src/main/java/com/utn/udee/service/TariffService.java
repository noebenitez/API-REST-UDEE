package com.utn.udee.service;

import com.utn.udee.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;
}
