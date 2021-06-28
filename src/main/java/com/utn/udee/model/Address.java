package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "The address cannot be null or whitespace.")
    @Column
    private String address;

    @NotNull(message = "The tariff must be specified. It cannot be null.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "tariff-address")
    @JoinColumn(name = "id_tariff", nullable = false)
    private Tariff tariff;

    @NotNull(message = "The customer must be specified. It cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "customer-address")
    @JoinColumn(name = "id_customer", nullable = false)
    private Client customer;

}
