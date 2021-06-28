package com.utn.udee.controller;

import com.utn.udee.exception.BrandNotExistsException;
import com.utn.udee.model.Brand;
import com.utn.udee.model.dto.BrandDto;
import com.utn.udee.service.BrandService;
import com.utn.udee.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addBrand(@RequestBody Brand brand)
    {
        Brand b = brandService.add(brand);
        return ResponseEntity.created(EntityURLBuilder.buildURL("brands",b.getId())).build();
    }
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getById(@PathVariable Integer id) throws BrandNotExistsException {
        Brand b =  brandService.getById(id);
        return ResponseEntity.ok(BrandDto.getBrandDto(b));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteBrandById(@PathVariable Integer id)
    {
        brandService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

}
