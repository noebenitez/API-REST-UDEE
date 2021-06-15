package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class FailTokenException extends GenericWebException{

    public FailTokenException(){
        this.status = HttpStatus.BAD_REQUEST;
        this.code = "07";
    }

    public String getMessage(){
        return "Used .";
    }
}
