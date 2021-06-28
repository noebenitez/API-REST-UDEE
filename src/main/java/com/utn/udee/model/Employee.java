package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
public class Employee extends User{

    public Employee(Integer id, String dni, String firstname, String lastname, String username, String password) {
        super(id, dni, firstname, lastname, username, password);
    }

    @Override
    public UserType userType() {
        return UserType.EMPLOYEE;
    }
}
