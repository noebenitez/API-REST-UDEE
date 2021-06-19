package com.utn.udee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MeasurementNotExistsException extends GenericWebException {
    public MeasurementNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "07";
    }

    public String getMessage(){
        return "Measurement doesnt exist";
    }
}
