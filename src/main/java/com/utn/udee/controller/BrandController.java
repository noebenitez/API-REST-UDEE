package com.utn.udee.controller;

import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.model.dto.BrandDto;
import com.utn.udee.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController("")
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity addBrand(@RequestBody Brand brand)
    {
        return brandService.add(brand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getById(@PathVariable Integer id) throws BrandNotExistsException {
        Brand b =  brandService.getById(id);
        return ResponseEntity.ok(BrandDto.getBrandDto(b));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteBrandById(@PathVariable Integer id)
    {
        brandService.deleteById(id);
        return ResponseEntity.ok(id);
    }

}
