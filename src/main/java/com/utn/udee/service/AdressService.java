package com.utn.udee.service;

import com.utn.udee.exception.AdressExistsException;
import com.utn.udee.exception.AdressNotExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.repository.AdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdressService {

    private final AdressRepository adressRepository;

    @Autowired
    public AdressService(AdressRepository adressRepository){
        this.adressRepository = adressRepository;
    }

    public Page<Adress> getAll(Pageable pageable) {
        return adressRepository.findAll(pageable);
    }

    public Adress add(Adress adress) throws AdressExistsException {
        if (!adressRepository.existsByAdress(adress.getAdress())){
            return adressRepository.save(adress);
        }else{
            throw new AdressExistsException();
        }
    }

    public Adress getById(Integer id) throws AdressNotExistsException {
        return adressRepository.findById(id)
                .orElseThrow(() -> new AdressNotExistsException());
    }

    public void deleteById(Integer id) throws AdressNotExistsException {
        if(adressRepository.existsById(id)){
            adressRepository.deleteById(id);
        }else{
            throw new AdressNotExistsException();
        }
    }

    public void update(Integer id, Adress newAdress) throws AdressNotExistsException {
        Adress adress = getById(id);
        adress.setAdress(newAdress.getAdress());
        adress.setCustomer(newAdress.getCustomer());
        adress.setTariff(newAdress.getTariff());
        adressRepository.save(adress);
    }
}
