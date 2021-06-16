package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "tariff-address")
    @JoinColumn(name = "id_tariff", nullable = false)
    private Tariff tariff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "customer-address")
    @JoinColumn(name = "id_customer", nullable = false)
    private Client customer;

}
