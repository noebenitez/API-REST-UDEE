package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class User {

    private Integer id;
    private Integer dni;
    private String firstname;
    private String lastname;
    private String password;
    private UserType userType;
}
