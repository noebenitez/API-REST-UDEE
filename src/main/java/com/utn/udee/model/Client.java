package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Client extends User{

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference(value = "customer-adress")
    private List<Address> addressList;

    public Client(Integer id, String dni, String firstname, String lastname, String username, String password, List<Address> addressList) {
        super(id, dni, firstname, lastname, username, password);
        this.addressList = addressList;
    }

    @Override
    public UserType userType() {
        return UserType.CLIENT;
    }
}