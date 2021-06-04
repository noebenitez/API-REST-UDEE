package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Employee extends User{

    private static UserType userType = UserType.EMPLOYEE;

    @Override
    public UserType userType() {
        return UserType.EMPLOYEE;
    }
}
