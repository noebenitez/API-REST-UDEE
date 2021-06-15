//package com.utn.udee.model;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name="invoices")
//public class Invoice {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @Column(name="initial_measurement")
//    private Float initialMeasurement;
//    @Column(name="final_measurement")
//    private Float finalMeasurement;
//    @Column(name="total_consumption")
//    private Float totalConsumption;
//    @Column(name="total_to_paid")
//    private Float totalToPaid;
//    private LocalDateTime initialTime;
//    private LocalDateTime finalTime;
//    private TariffType tariffType;
//}
