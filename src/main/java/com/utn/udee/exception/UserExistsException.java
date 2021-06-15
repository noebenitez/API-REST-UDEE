package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class UserExistsException extends GenericWebException {

    public UserExistsException(){
        this.status = HttpStatus.CONFLICT;
        this.code = "03";
    }

    public String getMessage() {
        return "User with the DNI number already exists.";
    }
}
