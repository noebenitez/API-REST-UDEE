package com.utn.udee.model.dto;

//import com.utn.udee.model.Invoice;
import com.utn.udee.model.Invoice;
import com.utn.udee.model.Measurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MeasurementDto {
    Integer id;
    Float measurement;
    Float price;
    LocalDateTime datetime;
    InvoiceDto invoice;


/*    public static MeasurementDto getMeasurementDto(Measurement measurement) {
        return MeasurementDto.builder().id(measurement.getId()).measurement(measurement.getMeasurement()).price(measurement.getPrice()).datetime(measurement.getDatetime()).build();
    }*/



}
