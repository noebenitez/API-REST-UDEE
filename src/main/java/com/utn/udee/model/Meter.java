package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message="Serial number must not be null or empty")
    private String serialNumber;
    @Column(name="m_password")
    private String password;
    @OneToOne
    @JoinColumn(name="id_brand")
    @NotNull(message="brand meter cannot be null")
    private Brand brand;
    @OneToOne
    @JoinColumn(name="id_model")
    @NotNull(message="meter model cannot be null")
    private Model model;
    @OneToOne
    @NotNull(message="address associated to meter cannot be null")
    @JoinColumn(name="id_address")
    private Address address;
    @OneToMany(mappedBy="meter", fetch=FetchType.EAGER)
    private List<Measurement> measurements;
//    @OneToMany(fetch=FetchType.EAGER)
//    private List<Invoice> invoices;



}
