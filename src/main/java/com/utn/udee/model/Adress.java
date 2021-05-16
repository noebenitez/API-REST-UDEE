package com.utn.udee.model;

import com.utn.udee.model.Tariff;
import com.utn.udee.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Adress {

    private Integer id;
    private String adress;
    private Tariff tariff;
    private User customer;

}
