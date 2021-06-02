package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class AdressNotExistsException extends GenericWebException {

    public AdressNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "05";
    }

    public String getMessage(){
        return "Adress don't exists.";
    }
}
