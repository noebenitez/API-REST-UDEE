package com.utn.udee.controller.converter;

import com.utn.udee.model.Adress;
import com.utn.udee.model.dto.AdressDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdressToAdressDTOConverter implements Converter<Adress, AdressDto> {

    private final ModelMapper modelMapper;

    public AdressToAdressDTOConverter(final ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public AdressDto convert(Adress adress) {
        return modelMapper.map(adress, AdressDto.class);
    }
}
