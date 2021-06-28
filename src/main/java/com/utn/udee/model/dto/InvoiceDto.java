package com.utn.udee.model.dto;

import com.utn.udee.model.PaymentStatus;
import com.utn.udee.model.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDto {
    String serialNumber;
    ClientDto billedTo;
    Float initialMeasurement;
    Float finalMeasurement;
    Float totalConsumption;
    Float amountDue;
    LocalDateTime initialTime;
    LocalDateTime endTime;
    LocalDateTime dueDate;
    TariffType tariffType;
    PaymentStatus paymentStatus;
}
