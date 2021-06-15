package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class TariffNotExistsException extends GenericWebException {

    public TariffNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "06";
    }

    public String getMessage(){
        return "Tariff don't exists.";
    }

}
