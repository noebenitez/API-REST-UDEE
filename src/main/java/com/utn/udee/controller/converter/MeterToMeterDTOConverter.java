package com.utn.udee.controller.converter;

import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeterDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeterToMeterDTOConverter implements Converter<Meter, MeterDto> { ///src,dest

    private final ModelMapper modelMapper;

    public MeterToMeterDTOConverter(final ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    @Override
    public MeterDto convert(Meter source) {
        return modelMapper.map(source,MeterDto.class);
    ///agarra del source por reflection vuelca todos los datos a meterdto
        ///habra fields que estaran en la source pero no en el output entonces no lo mapea
    }
}
