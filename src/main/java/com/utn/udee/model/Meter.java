package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Meter {

    private Integer id;
    private String serialNumber;
    private String password;
    private Brand brand;
    private Model model;
    private Adress adress;
    private List<Measurement> measurments;
    private List<Invoice> invoices;
}
