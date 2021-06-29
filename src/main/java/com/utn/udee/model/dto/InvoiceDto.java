package com.utn.udee.model.dto;

import com.utn.udee.model.Invoice;
import com.utn.udee.model.Measurement;
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

    public static InvoiceDto getInvoiceDto(Invoice invoice) {
        return InvoiceDto.builder().amountDue(invoice.getAmountDue()).dueDate(invoice.getDueDate()).billedTo(ClientDto.getClientDto(invoice.getBilledTo())).endTime(invoice.getEndTime()).initialTime(invoice.getInitialTime()).finalMeasurement(invoice.getFinalMeasurement()).paymentStatus(invoice.getPaymentStatus()).serialNumber(invoice.getSerialNumber()).tariffType(invoice.getTariffType()).totalConsumption(invoice.getTotalConsumption()).initialMeasurement(invoice.getInitialMeasurement()).build();
    }

}

