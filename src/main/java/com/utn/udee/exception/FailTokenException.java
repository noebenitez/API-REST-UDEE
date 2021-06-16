package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class FailTokenException extends GenericWebException{

    public FailTokenException(){
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = "07";
    }

    public String getMessage(){
        return "Cannot generate a token";
    }
}
