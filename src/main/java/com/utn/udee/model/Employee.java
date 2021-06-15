package com.utn.udee.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Employee extends User{

    private static UserType userType = UserType.EMPLOYEE;

    public Employee(Integer id, String dni, String firstname, String lastname, String username, String password) {
        super(id, dni, firstname, lastname, username, password);
    }

    @Override
    public UserType userType() {
        return UserType.EMPLOYEE;
    }
}
