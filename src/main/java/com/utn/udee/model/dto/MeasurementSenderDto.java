package com.utn.udee.model.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeasurementSenderDto {
    String serialNumber;
    float value;
    String date;
    String password;
}
