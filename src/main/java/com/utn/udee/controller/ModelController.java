package com.utn.udee.controller;

import com.utn.udee.exception.ModelNotExistsException;
import com.utn.udee.model.Model;
import com.utn.udee.model.dto.ModelDto;
import com.utn.udee.service.ModelService;
import com.utn.udee.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/models")
public class ModelController {

    private ModelService modelService;
    @Autowired
    public ModelController(ModelService modelService)
    {
        this.modelService=modelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelDto> getById(@PathVariable Integer id) throws ModelNotExistsException {
        Model m = modelService.getById(id);
        return ResponseEntity.ok(ModelDto.getModelDto(m));
    }

    @PostMapping
    public ResponseEntity addModel(@RequestBody Model model)
    {
        Model m=  modelService.add(model);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL("models",m.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id)
    {
         modelService.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
