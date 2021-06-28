package com.utn.udee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private Integer id;

    @NotBlank(message = "The address cannot be null or whitespace.")
    private String address;

    @NotNull(message = "The tariff must be specified. It cannot be null.")
    private TariffDto tariff;

    @NotNull(message = "The customer must be specified. It cannot be null.")
    private UserDto customer;
}
