package com.utn.udee.service;

import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.repository.BrandRepository;
import com.utn.udee.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository)
    {
        this.brandRepository = brandRepository;
    }

    public ResponseEntity add(Brand brand) {
        Brand b = brandRepository.save(brand);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL("brands",b.getId()))
                .build();

    }

    public Brand getById(Integer id) throws BrandNotExistsException {
        return brandRepository.findById(id).orElseThrow(() -> new BrandNotExistsException());
    }

    public void deleteById(Integer id) {
        brandRepository.deleteById(id);
    }
}
