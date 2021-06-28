package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name="measurements")
public class Measurement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="id_meter")
    @NotNull(message ="Meter associated cannot be null")
    private Meter meter;
    @Column(name="measurement")
    @NotNull(message="measurement cannot be null")
    private Float measurement;  //Measured in kw
    @Column
    private Float price;
    @Column(name="m_datetime")
    @NotNull(message = "date and time cannot be null")
    private LocalDateTime datetime;
    @OneToOne
    @JoinColumn(name="id_invoice")
    private Invoice invoice;
}
