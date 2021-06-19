package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class BrandNotExistsException extends GenericWebException {
    public BrandNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "06";
    }

    public String getMessage(){
        return "Brand doesnt exist.";
    }
}
