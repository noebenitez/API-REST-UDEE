package com.utn.udee.model.dto;

import com.utn.udee.model.Meter;
import com.utn.udee.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDto {
    String model;

    public static ModelDto getModelDto(Model m) {
        return ModelDto.builder().model(m.getModel()).build();
    }
}
