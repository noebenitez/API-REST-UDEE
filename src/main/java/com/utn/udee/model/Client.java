package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Client extends User{

    private List<Adress> adressList;
}
