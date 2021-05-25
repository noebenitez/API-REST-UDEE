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
    private List<Adress> adressList;


    @Override
    public UserType userType() {
        return UserType.CLIENT;
    }
}
