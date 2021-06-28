package com.utn.udee.model.dto;
import com.utn.udee.model.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffDto {

    private Integer id;

    @PositiveOrZero(message = "The tariff must be specified. It cannot be a negative value.")
    private Float tariff;

    @NotNull(message = "The tariffType must be specified. It cannot be null.")
    private TariffType tariffType;

}
