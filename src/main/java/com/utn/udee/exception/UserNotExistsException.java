package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class UserNotExistsException extends GenericWebException{

    public UserNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "06";
    }

    public String getMessage(){
        return "User don't exists.";
    }
}
