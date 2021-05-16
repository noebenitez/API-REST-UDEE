package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tariff {

    private Integer id;
    private Float tariff;
    private TariffType tariffType;
}
