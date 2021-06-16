package com.utn.udee.service;

import com.utn.udee.exception.TariffExistsException;
import com.utn.udee.exception.TariffNotExistsException;
import com.utn.udee.model.Tariff;
import com.utn.udee.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository){
        this.tariffRepository = tariffRepository;
    }

    public Tariff add(Tariff tariff) throws TariffExistsException {
        if(!tariffRepository.existsTariffByTariffAndTariffType(tariff.getTariff(), tariff.getTariffType())){
            return tariffRepository.save(tariff);
        }else{
            throw new TariffExistsException();
        }
    }

    public Page<Tariff> getAll(Pageable pageable) {
        return tariffRepository.findAll(pageable);
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
        tariff.setAddresses(newTariff.getAddresses());
        tariffRepository.save(tariff);
    }
}
