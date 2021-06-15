package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class TariffExistsException extends GenericWebException{

    public TariffExistsException(){
        this.status = HttpStatus.CONFLICT;
        this.code = "01";
    }

    public String getMessage(){
        return "Tariff already exists.";
    }
}
