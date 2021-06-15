package com.utn.udee.controller.converter;

import com.utn.udee.model.Address;
import com.utn.udee.model.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdressToAdressDTOConverter implements Converter<Address, AddressDto> {

    private final ModelMapper modelMapper;

    public AdressToAdressDTOConverter(final ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDto convert(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }
}
