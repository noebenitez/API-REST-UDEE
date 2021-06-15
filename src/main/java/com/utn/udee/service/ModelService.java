package com.utn.udee.service;

import com.utn.udee.exception.ModelNotExistsException;
import com.utn.udee.model.Model;
import com.utn.udee.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository)
    {
        this.modelRepository = modelRepository;
    }

    public Model getById(Integer id) throws ModelNotExistsException {
         return modelRepository.findById(id).orElseThrow(ModelNotExistsException::new);
    }

    public Model add(Model model) {
        return modelRepository.save(model);

    }


    public void deleteById(Integer id) {
    }
}
