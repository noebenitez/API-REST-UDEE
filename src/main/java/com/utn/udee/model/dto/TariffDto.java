package com.utn.udee.model.dto;
import com.utn.udee.model.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffDto {

    private Integer id;
    private Float tariff;
    private TariffType tariffType;

}
