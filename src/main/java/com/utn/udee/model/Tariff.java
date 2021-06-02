package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor

@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Float tariff;

    @Column(name = "tariff_type")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;

    @JsonManagedReference(value = "tariff-adress")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tariff")
    private List<Adress> adresses;

}
