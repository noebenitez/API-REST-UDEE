package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private List<Adress> adressList;

    public Client(Integer id, String dni, String firstname, String lastname, String username, String password, List<Adress> adressList) {
        super(id, dni, firstname, lastname, username, password);
        this.adressList = adressList;
    }

    @Override
    public UserType userType() {
        return UserType.CLIENT;
    }
}
