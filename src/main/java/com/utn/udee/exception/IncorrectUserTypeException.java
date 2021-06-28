package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class IncorrectUserTypeException extends GenericWebException{

    public IncorrectUserTypeException(){
        this.status = HttpStatus.CONFLICT;
        this.code = "08";
    }

    public String getMessage(){
        return "User type doesn't support the operation.";
    }
}
