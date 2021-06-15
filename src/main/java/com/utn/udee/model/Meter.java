package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name="meters")
@AllArgsConstructor
public class Meter {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="serial_number")
    @NotNull(message="Serial number must not be null")
    private String serialNumber;
    @Column(name="m_password")
    private String password;
    @OneToOne
    @JoinColumn(name="id_brand")
    private Brand brand;
    @OneToOne
    @JoinColumn(name="id_model")
    private Model model;
    @OneToOne
    @NotNull(message="address associated to meter cannot be null")
    @JoinColumn(name="id_adress")
    private Address address;
    @OneToMany(mappedBy="meter", fetch=FetchType.EAGER)
    private List<Measurement> measurements;
//    @OneToMany(fetch=FetchType.EAGER)
//    private List<Invoice> invoices;



}
