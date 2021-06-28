package com.utn.udee.controller.converter;

import com.utn.udee.model.Measurement;
import com.utn.udee.model.dto.MeasurementDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeasurementToMeasurementDTOConverter implements Converter<Measurement,MeasurementDto> {

    private final ModelMapper modelMapper;

    public MeasurementToMeasurementDTOConverter(final ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public MeasurementDto convert(Measurement source) {
        return modelMapper.map(source, MeasurementDto.class);
    }
}
