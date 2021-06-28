package com.utn.udee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MeterSameAddressExistsException extends GenericWebException {
    public MeterSameAddressExistsException(){
        this.status = HttpStatus.CONFLICT;
        this.code = "09";
    }

    public String getMessage(){
        return "A Meter with the same address already exists";
    }
}