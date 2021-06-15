package com.utn.udee.model.dto;

import com.utn.udee.model.Brand;
import com.utn.udee.model.Measurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BrandDto {

    public String brand;

    public static BrandDto getBrandDto(Brand brand) {
        return BrandDto.builder().brand(brand.getBrand()).build();
    }
}
