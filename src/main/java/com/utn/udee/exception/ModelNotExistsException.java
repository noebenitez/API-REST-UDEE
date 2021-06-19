package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class ModelNotExistsException extends GenericWebException {
    public ModelNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "09";
    }

    public String getMessage(){
        return "specified model doesnt exist";
    }
}
