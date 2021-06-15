package com.utn.udee.utils;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ListMapperDto {

    public static <F,T> List<F> getListDto(ModelMapper modelMapper,List<T> entityList, Class<F> dtoType ){
        return entityList.stream().map(obj -> modelMapper.map(obj, dtoType)).collect(Collectors.toList());
    }
}