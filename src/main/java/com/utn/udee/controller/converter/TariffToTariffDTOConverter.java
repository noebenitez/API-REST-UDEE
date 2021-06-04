package com.utn.udee.controller.converter;

import com.utn.udee.model.Tariff;
import com.utn.udee.model.dto.TariffDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TariffToTariffDTOConverter implements Converter<Tariff, TariffDto> {

    private final ModelMapper modelMapper;

    public TariffToTariffDTOConverter(final ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public TariffDto convert(Tariff tariff) {
        return modelMapper.map(tariff, TariffDto.class);
    }

}
