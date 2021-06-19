package com.utn.udee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MeterNotExistsException extends GenericWebException {
    public MeterNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "08";
    }

    public String getMessage(){
        return "meter doesnt exist";
    }
}
