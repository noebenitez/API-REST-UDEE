package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "adresses")
public class Adress {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String adress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "tariff-adress")
    @JoinColumn(name = "id_tariff", nullable = false)
    private Tariff tariff;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "customer-adress")
    @JoinColumn(name = "id_customer", nullable = false)
    private User customer;

}
