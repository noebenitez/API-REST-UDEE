package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="serial_number")
    @NotEmpty(message="Serial number associated cannot be left null or empty")
    private String serialNumber;
    @OneToOne
    @Column(name="id_customer")
    @NotNull(message="client associated with invoice cannot be null")
    private Client client;
    @Column(name="initial_measurement")
    private Float initialMeasurement;
    @Column(name="final_measurement")
    private Float finalMeasurement;
    @NotNull(message="Total consumption cannot be null")
    @Column(name="total_consumption")
    private Float totalConsumption;
    @NotNull(message="Total to be paid cannot be null")
    @Column(name="total_to_paid")
    private Float totalToPaid;
    @Column(name="initial_datetime")
    private LocalDateTime initialTime;
    @Column(name="final_datetime")
    private LocalDateTime finalTime;
    @Column(name="due_date")
    private LocalDateTime dueDate;
    @NotNull(message="Tariff type cannot be null")
    @Column(name="tariffType")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;
    @Enumerated(EnumType.STRING)
    @NotNull(message="payment status cannot be left null")
    @Column(name="payment_status")
    private PaymentStatus paymentStatus;
}
