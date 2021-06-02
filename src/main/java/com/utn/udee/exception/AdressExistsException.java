package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class AdressExistsException extends GenericWebException {

    public AdressExistsException(){
        this.status = HttpStatus.CONFLICT;
        this.code = "04";
    }

    public String getMessage(){
        return "Adress already exists.";
    }
}
