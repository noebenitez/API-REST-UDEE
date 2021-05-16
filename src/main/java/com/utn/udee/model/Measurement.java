package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Measurement {

    private Integer id;
    private Float measurement;  //Measured in kw
    private Float price;
    private LocalDateTime datetime;
    private Invoice invoice;
}
