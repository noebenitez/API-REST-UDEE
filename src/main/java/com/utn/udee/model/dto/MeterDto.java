package com.utn.udee.model.dto;

import com.utn.udee.model.Address;
import com.utn.udee.model.Brand;
import com.utn.udee.model.Meter;
import com.utn.udee.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterDto {
    String serialNumber;

   Brand brand;

    Model model;

     Address address;

    public static MeterDto getMeterDto(Meter meter) {
        return MeterDto.builder().serialNumber(meter.getSerialNumber()).brand(meter.getBrand()).model(meter.getModel()).build();
    }

}
