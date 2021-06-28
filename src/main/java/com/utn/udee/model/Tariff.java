package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @PositiveOrZero(message = "The tariff must be specified. It cannot be a negative value.")
    @Column
    private Float tariff;

    @NotBlank(message = "The tariffType must be specified. It cannot be null or whitespace.")
    @Column(name = "tariff_type")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;

    @JsonManagedReference(value = "tariff-address")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariff", orphanRemoval = true)
    private List<Address> addresses;

}
