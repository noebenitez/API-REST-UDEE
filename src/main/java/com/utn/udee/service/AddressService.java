package com.utn.udee.service;

import com.utn.udee.exception.AddressExistsException;
import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.model.Address;
import com.utn.udee.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public Page<Address> getAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Address add(Address address) throws AddressExistsException {
        if (!addressRepository.existsByAddress(address.getAddress())){
            return addressRepository.save(address);
        }else{
            throw new AddressExistsException();
        }
    }

    public Address getById(Integer id) throws AddressNotExistsException {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotExistsException());
    }

    public void deleteById(Integer id) throws AddressNotExistsException {
        if(addressRepository.existsById(id)){
            addressRepository.deleteById(id);
        }else{
            throw new AddressNotExistsException();
        }
    }

    public void update(Integer id, Address newAddress) throws AddressNotExistsException {
        Address address = getById(id);
        address.setAddress(newAddress.getAddress());
        address.setCustomer(newAddress.getCustomer());
        address.setTariff(newAddress.getTariff());
        addressRepository.save(address);
    }
}