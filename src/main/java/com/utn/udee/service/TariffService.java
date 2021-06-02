package com.utn.udee.service;

import com.utn.udee.exception.AdressNotExistsException;
import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Adress;
import com.utn.udee.model.Tariff;
import com.utn.udee.repository.TariffRepository;
import com.utn.udee.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    public Tariff add(Tariff tariff) throws TariffExistsException {
        //Se va a cambiar por verificar si no hay una tarifa con mismo tipo y valor
        if(tariff.getId() == null || !tariffRepository.existsById(tariff.getId())){
            return tariffRepository.save(tariff);
        }else{
            throw new TariffExistsException();
        }
    }

    public Page<Tariff> getAll(Pageable pageable) {
        return tariffRepository.findAll(pageable);
    }

    public List<Adress> getList() {
        return tariffRepository.findById(7).get().getAdresses();
    }

    public void deleteById(Integer id) throws TariffNotExistsException {
        if(tariffRepository.existsById(id)){
            tariffRepository.deleteById(id);
        }else{
            throw new TariffNotExistsException();
        }
    }

    public Tariff getById(Integer id) throws TariffNotExistsException {
        return tariffRepository.findById(id)
                .orElseThrow(TariffNotExistsException::new);
    }

    public void update(Integer id, Tariff newTariff) throws TariffNotExistsException {
        Tariff tariff = getById(id);
        tariff.setTariff(newTariff.getTariff());
        tariff.setTariffType(newTariff.getTariffType());
        tariff.setAdresses(newTariff.getAdresses());
        tariffRepository.save(tariff);
    }
}
