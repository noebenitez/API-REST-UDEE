package com.utn.udee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdressDto {

    private Integer id;
    private String adress;
    private TariffDto tariff;
}
