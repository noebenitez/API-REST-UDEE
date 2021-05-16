package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Invoice {

    private Integer id;
    private Float initialMeasurement;
    private Float finalMeasurement;
    private Float totalConsumption;
    private Float totalToPaid;
    private LocalDateTime initialTime;
    private LocalDateTime finalTime;
    private TariffType tariffType;
}
